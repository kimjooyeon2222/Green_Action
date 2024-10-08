package com.example.green_action.Community;

public class Comment {
    private String commentId;
    private String userId;       // 사용자의 ID
    private String username;     // 사용자의 이름
    private String commentText;
    private long timestamp;

    // 기본 생성자
    public Comment() {}

    // 생성자
    public Comment(String commentId, String userId, String username, String commentText, long timestamp) {
        this.commentId = commentId;
        this.userId = userId;
        this.username = username;
        this.commentText = commentText;
        this.timestamp = timestamp;
    }

    // Getter and Setter 메서드
    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
