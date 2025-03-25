# GitHub User Activity  

A command-line tool to fetch and display a GitHub user's recent activity.  

## 🔹 Features  

- Fetch recent GitHub activity for any user  
- Display events like pushes, issues, forks, stars, and more  
- Interactive and non-interactive modes  
- Simple and intuitive commands  

## 🚀 Installation  

### Prerequisites  
- Java JDK 8+  
- Git (optional)  

### Steps  
1. Clone the repo:  
   ```bash
   git clone https://github.com/adninzxc/github-user-activity.git
   cd github-user-activity
   ```  
2. Compile:  
   ```bash
   javac -d bin src/Main.java src/api/GitHubAPIClient.java src/model/*.java
   ```  
3. Run:  
   ```bash
   java -cp bin Main
   ```  

## 💻 Usage  

### Interactive Mode  
Run without arguments:  
```bash
java -cp bin Main
```  
Enter commands like:  
- `user <username>` – Fetch user activity  
- `help` – Show commands  
- `exit` – Quit  

### Non-Interactive Mode  
Fetch a user’s activity directly:  
```bash
java -cp bin Main octocat
```  

## 📁 Project Structure  
```
github-user-activity/
├── src/
│   ├── Main.java                   # Entry point and CLI interface
│   ├── api/
│   │   └── GitHubAPIClient.java    # Handles GitHub API interactions
│   └── model/
│       ├── Commit.java             # Model for commit data
│       ├── GitHubEvent.java        # Base model for GitHub events
│       ├── GitHubForkEvent.java    # Model for fork events
│       ├── GitHubIssueEvent.java   # Model for issue events
│       ├── GitHubPayload.java      # Model for event payloads
│       ├── GitHubPushEvent.java    # Model for push events
│       ├── GitHubRepo.java         # Model for repository data
│       ├── GitHubStarEvent.java    # Model for star events
│       ├── Issue.java              # Model for issue data
│       └── IssuePayload.java       # Model for issue-specific payload
└── README.md                       # This file
```  

## 🔧 Technical Details  
- Uses `https://api.github.com/users/{username}/events`  
- Custom JSON parser (no external dependencies)  
- Plans for authentication, pagination, and more event support  

## 🙌 Credits  
- [GitHub API Docs](https://docs.github.com/en/rest)  
- [Roadmap.sh](https://roadmap.sh/projects/github-user-activity)  

---  

Created by [adninzxc](https://github.com/adninzxc) – feel free to reach out! 🚀
