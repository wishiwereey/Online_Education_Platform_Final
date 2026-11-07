package observer;
import java.util.HashMap;
import java.util.Map;
public class ProgressTracker implements LearningObserver {
    private final Map<String, Integer> gradedCount = new HashMap<>();
    @Override
    public void update(LearningEvent e) {
        if (e.type() == EventType.ASSIGNMENT_GRADED && !e.isBroadcast()) {
            gradedCount.merge(e.student().getId(), 1, Integer::sum);
        }
    }
    public int gradedAssignments(String studentId) {
        return gradedCount.getOrDefault(studentId, 0);
    }
}
