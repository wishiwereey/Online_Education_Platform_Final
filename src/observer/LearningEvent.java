package observer;
import model.Student;
public record LearningEvent(EventType type, Student student, String courseId, String message) {
    public boolean isBroadcast() {
        return student == null;
    }
}
