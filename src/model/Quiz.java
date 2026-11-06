package model;


public final class Quiz {
    private final String title;
    private final int questionCount;
    private final int passingScore;

    public Quiz(String title, int questionCount, int passingScore) {
        if (questionCount <= 0) throw new IllegalArgumentException("Quiz needs at least one question");
        if (passingScore < 0 || passingScore > 100) throw new IllegalArgumentException("Passing score must be 0..100");
        this.title = title;
        this.questionCount = questionCount;
        this.passingScore = passingScore;
    }

    public String getTitle() { return title; }
    public int getQuestionCount() { return questionCount; }
    public int getPassingScore() { return passingScore; }

    @Override
    public String toString() {
        return title + " (" + questionCount + " questions, pass >= " + passingScore + "%)";
    }
}
