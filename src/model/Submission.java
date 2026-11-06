package model;
import factory.Assignment;
public class Submission {
    private final Student student;
    private final Assignment assignment;
    private final String content;
    private Integer score;
    private String feedback;

    public Submission(Student student, Assignment assignment, String content) {
        this.student = student;
        this.assignment = assignment;
        this.content = content;
    }

    public Student getStudent() { return student; }
    public Assignment getAssignment() { return assignment; }
    public String getContent() { return content; }
    public Integer getScore() { return score; }
    public String getFeedback() { return feedback; }
    public boolean isGraded() { return score != null; }

    public void grade(int score, String feedback) {
        if (score < 0 || score > assignment.getMaxScore()) {
            throw new IllegalArgumentException("Score must be between 0 and " + assignment.getMaxScore());
        }
        this.score = score;
        this.feedback = feedback;
    }
}
