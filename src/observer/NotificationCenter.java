package observer;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
public class NotificationCenter {
    private final Map<EventType, List<LearningObserver>> subscribers = new EnumMap<>(EventType.class);
    public void subscribe(LearningObserver observer, EventType... types) {
        EventType[] chosen = types.length == 0 ? EventType.values() : types;
        for (EventType type : chosen) {
            List<LearningObserver> list = subscribers.computeIfAbsent(type, t -> new ArrayList<>());
            if (!list.contains(observer)) list.add(observer);
        }
    }
    public void unsubscribe(LearningObserver observer) {
        subscribers.values().forEach(list -> list.remove(observer));
    }
    public void publish(LearningEvent event) {
        for (LearningObserver o : List.copyOf(subscribers.getOrDefault(event.type(), List.of()))) {
            o.update(event);
        }
    }
    public int subscriberCount(EventType type) {
        return subscribers.getOrDefault(type, List.of()).size();
    }
}