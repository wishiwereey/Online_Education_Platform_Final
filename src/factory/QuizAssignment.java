package factory;
import java.time.LocalDate;
public class QuizAssignment extends AbstractAssignment {
    private final int questions;
    QuizAssignment(String id, String title, String courseId, LocalDate deadline, int questions) {
        super(id, title, courseId, deadline, questions * 2); // 2 points per question
        this.questions = questions;
    }
    public int getQuestions() {
        return questions;
    }
    @Override public String getType() {
        return "Quiz";
    }
    @Override public String getInstructions() {
        return "Answer " + questions + " multiple-choice questions. Checked automatically.";
    }
}
