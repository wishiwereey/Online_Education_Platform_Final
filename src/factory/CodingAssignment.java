package factory;
import java.time.LocalDate;
public class CodingAssignment extends AbstractAssignment {
    private final String language;
    private final int testCount;
    CodingAssignment(String id, String title, String courseId, LocalDate deadline, String language, int testCount) {
        super(id, title, courseId, deadline, testCount * 10); // 10 points per test
        this.language = language;
        this.testCount = testCount;
    }
    public String getLanguage() {
        return language;
    }
    public int getTestCount() {
        return testCount;
    }
    @Override public String getType() {
        return "Coding";
    }
    @Override public String getInstructions() {
        return "Solve the task in " + language + ". Your code must pass " + testCount + " unit tests.";
    }
}
