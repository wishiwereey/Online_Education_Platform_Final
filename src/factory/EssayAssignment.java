package factory;
import java.time.LocalDate;
public class EssayAssignment extends AbstractAssignment {
    private final int minWords;
    EssayAssignment(String id, String title, String courseId, LocalDate deadline, int minWords) {
        super(id, title, courseId, deadline, 100);
        this.minWords = minWords;
    }
    public int getMinWords() {
        return minWords;
    }
    @Override public String getType() {
        return "Essay";
    }
    @Override public String getInstructions() {
        return "Write at least " + minWords + " words. Graded by the instructor.";
    }
}
