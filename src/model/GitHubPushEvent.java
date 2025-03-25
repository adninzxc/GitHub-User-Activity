package model;

import java.util.List;

public class GitHubPushEvent {
    private String type;
    private GitHubRepo repo;
    private List<Commit> commits;

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    public GitHubRepo getRepo() {return repo;}
    public void setRepo(GitHubRepo repo) {this.repo = repo;}

    public List<Commit> getCommits() {return commits;}
    public void setCommits(List<Commit> commits) {this.commits = commits;}
}

