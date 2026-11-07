package factory;
import java.time.LocalDate;
abstract class AbstractAssignment implements Assignment {
    private final String id;
    private final String title;
    private final String courseId;
    private final LocalDate deadline;
    private final int maxScore;
    protected AbstractAssignment(String id, String title, String courseId, LocalDate deadline, int maxScore) {
        this.id = id;
        this.title = title;
        this.courseId = courseId;
        this.deadline = deadline;
        this.maxScore = maxScore;
    }
    @Override public String getId() {
        return id;
    }
    @Override public String getTitle() {
        return title;
    }
    @Override public String getCourseId() {
        return courseId;
    }
    @Override public LocalDate getDeadline() {
        return deadline;
    }
    @Override public int getMaxScore() {
        return maxScore;
    }
    @Override
    public String toString() {
        return String.format("%s '%s' [%s] max %d pts, due %s", getType(), title, id, maxScore, deadline);
    }
}
