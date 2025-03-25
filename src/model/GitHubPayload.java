package model;

import java.util.List;

public class GitHubPayload {
    private List<Commit> commits;

    public List<Commit> getCommits() {return commits;}
    public void setCommits(List<Commit> commits) {this.commits = commits;}
}
