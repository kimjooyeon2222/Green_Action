package com.example.green_action;

public class DailyQuiz {
    public int quizId;
    public String question;
    public String correctAnswer;
    public int correctAnswerLength;
    public int maxScore;
    public int attemptsAllowed;
    public String explanation;
    public long timestamp;

    public DailyQuiz() {}

    public DailyQuiz(int quizId, String question, String correctAnswer, int correctAnswerLength, int maxScore, int attemptsAllowed, String explanation, long timestamp) {
        this.quizId = quizId;
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.correctAnswerLength = correctAnswerLength;
        this.maxScore = maxScore;
        this.attemptsAllowed = attemptsAllowed;
        this.explanation = explanation;
        this.timestamp = timestamp;
    }
}