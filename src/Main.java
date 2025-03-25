import java.util.Scanner;
import java.util.List;
import static api.GitHubAPIClient.fetchGitHubEvents;
import model.GitHubEvent;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean interactive = true;

        System.out.println("Welcome to GitHub User Activity! If you need help with the commands, type 'help'");

        if (args.length > 0) {
            String username = args[0];
            List<GitHubEvent> events = fetchGitHubEvents(username);
            displayEvents(username, events);
        }

        while (interactive) {
            System.out.print("github-activity> ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
                System.out.println("Goodbye!");
                break;
            } else if (input.equalsIgnoreCase("help")) {
                showHelp();
            } else if (input.startsWith("user ")) {
                String username = input.substring(5).trim();
                if (!username.isEmpty()) {
                    List<GitHubEvent> events = fetchGitHubEvents(username);
                    displayEvents(username, events);
                } else {
                    System.out.println("Please provide a username: user <github-username>");
                }
            } else if (!input.isEmpty()) {
                System.out.println("Unknown command. Type 'help' for available commands.");
            }
        }

        scanner.close();
    }

    private static void showHelp() {
        System.out.println("\nAvailable commands:");
        System.out.println("  user <github-username> - Fetch and display GitHub activity for a user");
        System.out.println("  help                   - Show this help message");
        System.out.println("  exit, quit             - Exit the application\n");
    }

    private static void displayEvents(String username, List<GitHubEvent> events) {
        if (events == null || events.isEmpty()) {
            System.out.println("No recent activity found for " + username);
        } else {
            System.out.println("\nRecent Activity for " + username + ":");
            for (GitHubEvent event : events) {
                System.out.println("- " + event);
            }
            System.out.println();
        }
    }
}