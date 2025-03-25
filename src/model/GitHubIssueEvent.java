package model;

public class GitHubIssueEvent {
    private String type;
    private GitHubRepo repo;
    private Issue issue;

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    public GitHubRepo getRepo() {return repo;}
    public void setRepo(GitHubRepo repo) {this.repo = repo;}

    public Issue getIssue() {return issue;}
    public void setIssue(Issue issue) {this.issue = issue;}
}
