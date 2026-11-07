package observer;
import java.util.LinkedHashMap;
import java.util.Map;
public class InstructorDashboard implements LearningObserver {
    private final Map<EventType, Integer> counters = new LinkedHashMap<>();
    @Override
    public void update(LearningEvent e) {
        counters.merge(e.type(), 1, Integer::sum);
    }
    public int count(EventType type) {
        return counters.getOrDefault(type, 0);
    }
    public Map<EventType, Integer> snapshot() {
        return Map.copyOf(counters);
    }
}
