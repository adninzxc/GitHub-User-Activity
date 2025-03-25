package model;

public class GitHubEvent {
    private String type;
    private GitHubRepo repo;
    private GitHubPayload payload;
    private Object specializedPayload; // Can be IssuePayload, etc.

    public GitHubEvent(String type, GitHubRepo repo) {
        this.type = type;
        this.repo = repo;
    }

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    public GitHubRepo getRepo() {return repo;}
    public void setRepo(GitHubRepo repo) {this.repo = repo;}

    public GitHubPayload getPayload() {return payload;}
    public void setPayload(GitHubPayload payload) {this.payload = payload;}

    public Object getSpecializedPayload() {return specializedPayload;}
    public void setSpecializedPayload(Object specializedPayload) {this.specializedPayload = specializedPayload;}

    @Override
    public String toString() {
        String result = formatEventType(type) + " on " + repo.getName();

        if (type.equals("PushEvent") && payload != null && payload.getCommits() != null) {
            int commitCount = payload.getCommits().size();
            result += String.format(" (%d commit%s)",
                    commitCount, commitCount == 1 ? "" : "s");

            if (commitCount > 0) {
                result += ": " + payload.getCommits().get(0).getMessage();
                if (commitCount > 1) {
                    result += " (+" + (commitCount - 1) + " more)";
                }
            }
        } else if ((type.equals("IssuesEvent") || type.equals("IssueCommentEvent"))
                && specializedPayload instanceof IssuePayload) {
            IssuePayload issuePayload = (IssuePayload) specializedPayload;
            if (issuePayload.getIssue() != null) {
                result += ": " + issuePayload.getIssue().getTitle();
            }
        }

        return result;
    }

    private String formatEventType(String type) {
        switch (type) {
            case "PushEvent": return "Pushed code";
            case "CreateEvent": return "Created";
            case "ForkEvent": return "Forked";
            case "IssuesEvent": return "Updated issue";
            case "IssueCommentEvent": return "Commented on issue";
            case "PullRequestEvent": return "Pull request";
            case "WatchEvent": return "Starred";
            default: return type;
        }
    }
}