package model;

public class GitHubStarEvent {
    private String type;
    private GitHubRepo repo;

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    public GitHubRepo getRepo() {return repo;}
    public void setRepo(GitHubRepo repo) {this.repo = repo;}
}
