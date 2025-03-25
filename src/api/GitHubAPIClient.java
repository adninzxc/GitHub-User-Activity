// Updated GitHubAPIClient.java - Using manual JSON parsing without external libraries
package api;

import model.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GitHubAPIClient {
    public static List<GitHubEvent> fetchGitHubEvents(String username) {
        String apiUrl = "https://api.github.com/users/" + username + "/events";
        List<GitHubEvent> events = new ArrayList<>();

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                events = parseEvents(response.toString());
            } else {
                System.out.println("Error: Unable to fetch events. HTTP Response Code: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return events;
    }

    private static List<GitHubEvent> parseEvents(String jsonResponse) {
        List<GitHubEvent> events = new ArrayList<>();

        if (!jsonResponse.startsWith("[") || !jsonResponse.endsWith("]")) {
            return events;
        }

        String eventsJson = jsonResponse.substring(1, jsonResponse.length() - 1);
        List<String> eventObjects = splitJsonObjects(eventsJson);

        for (String eventJson : eventObjects) {
            try {
                String type = extractJsonValue(eventJson, "\"type\"");
                if (type == null) continue;

                String repoJson = extractJsonObject(eventJson, "\"repo\"");
                if (repoJson == null) continue;

                String repoName = extractJsonValue(repoJson, "\"name\"");
                if (repoName == null) continue;

                GitHubRepo repo = new GitHubRepo(repoName);
                GitHubEvent event = new GitHubEvent(type, repo);

                String payloadJson = extractJsonObject(eventJson, "\"payload\"");
                if (payloadJson != null) {
                    GitHubPayload payload = new GitHubPayload();

                    if (type.equals("PushEvent")) {
                        String commitsJson = extractJsonArray(payloadJson, "\"commits\"");
                        if (commitsJson != null) {
                            List<String> commitObjects = splitJsonObjects(commitsJson);
                            List<Commit> commits = new ArrayList<>();

                            for (String commitJson : commitObjects) {
                                String message = extractJsonValue(commitJson, "\"message\"");
                                if (message != null) {
                                    Commit commit = new Commit();
                                    commit.setMessage(message);
                                    commits.add(commit);
                                }
                            }

                            payload.setCommits(commits);
                        }
                    }

                    if (type.equals("IssuesEvent") || type.equals("IssueCommentEvent")) {
                        String issueJson = extractJsonObject(payloadJson, "\"issue\"");
                        if (issueJson != null) {
                            String title = extractJsonValue(issueJson, "\"title\"");
                            if (title != null) {
                                Issue issue = new Issue();
                                issue.setTitle(title);

                                IssuePayload issuePayload = new IssuePayload();
                                issuePayload.setIssue(issue);
                                event.setSpecializedPayload(issuePayload);
                            }
                        }
                    }

                    event.setPayload(payload);
                }

                events.add(event);
            } catch (Exception e) {
                System.out.println("Warning: Skipped an event due to parsing error: " + e.getMessage());
            }
        }

        return events;
    }

    private static String extractJsonValue(String json, String key) {
        int keyIndex = json.indexOf(key);
        if (keyIndex == -1) return null;

        int valueStartIndex = json.indexOf(":", keyIndex) + 1;
        while (valueStartIndex < json.length() && Character.isWhitespace(json.charAt(valueStartIndex))) {
            valueStartIndex++;
        }

        if (valueStartIndex >= json.length()) return null;

        if (json.charAt(valueStartIndex) == '\"') {
            valueStartIndex++;
            int valueEndIndex = json.indexOf("\"", valueStartIndex);
            if (valueEndIndex == -1) return null;
            return json.substring(valueStartIndex, valueEndIndex);
        } else {
            int valueEndIndex = json.length();
            for (char endChar : new char[] {',', '}'}) {
                int endPos = json.indexOf(endChar, valueStartIndex);
                if (endPos != -1 && endPos < valueEndIndex) {
                    valueEndIndex = endPos;
                }
            }
            return json.substring(valueStartIndex, valueEndIndex).trim();
        }
    }

    private static String extractJsonObject(String json, String key) {
        int keyIndex = json.indexOf(key);
        if (keyIndex == -1) return null;

        int objectStartIndex = json.indexOf("{", keyIndex);
        if (objectStartIndex == -1) return null;

        int braceCount = 1;
        int objectEndIndex = objectStartIndex + 1;

        while (braceCount > 0 && objectEndIndex < json.length()) {
            char c = json.charAt(objectEndIndex);
            if (c == '{') braceCount++;
            else if (c == '}') braceCount--;
            objectEndIndex++;
        }

        if (braceCount != 0) return null;
        return json.substring(objectStartIndex, objectEndIndex);
    }

    private static String extractJsonArray(String json, String key) {
        int keyIndex = json.indexOf(key);
        if (keyIndex == -1) return null;

        int arrayStartIndex = json.indexOf("[", keyIndex);
        if (arrayStartIndex == -1) return null;

        int bracketCount = 1;
        int arrayEndIndex = arrayStartIndex + 1;

        while (bracketCount > 0 && arrayEndIndex < json.length()) {
            char c = json.charAt(arrayEndIndex);
            if (c == '[') bracketCount++;
            else if (c == ']') bracketCount--;
            arrayEndIndex++;
        }

        if (bracketCount != 0) return null;
        return json.substring(arrayStartIndex + 1, arrayEndIndex - 1);
    }

    private static List<String> splitJsonObjects(String jsonArray) {
        List<String> objects = new ArrayList<>();

        int startIndex = 0;
        int braceCount = 0;
        boolean inQuotes = false;
        boolean escapeNext = false;

        for (int i = 0; i < jsonArray.length(); i++) {
            char c = jsonArray.charAt(i);

            if (escapeNext) {
                escapeNext = false;
                continue;
            }

            if (c == '\\') {
                escapeNext = true;
                continue;
            }

            if (c == '\"') {
                inQuotes = !inQuotes;
            }

            if (!inQuotes) {
                if (c == '{') {
                    if (braceCount == 0) {
                        startIndex = i;
                    }
                    braceCount++;
                } else if (c == '}') {
                    braceCount--;
                    if (braceCount == 0) {
                        objects.add(jsonArray.substring(startIndex, i + 1));
                    }
                }
            }
        }

        return objects;
    }
}