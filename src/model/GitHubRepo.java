package model;

public class GitHubRepo {
    private String name;

    public GitHubRepo(String repoName) {
        this.name = repoName;
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
}

