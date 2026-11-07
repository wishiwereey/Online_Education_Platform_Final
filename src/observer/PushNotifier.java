package observer;
public class PushNotifier implements LearningObserver {
    private final NotificationLog log;
    public PushNotifier(NotificationLog log) {
        this.log = log;
    }
    @Override
    public void update(LearningEvent e) {
        String to = e.isBroadcast() ? "all students" : e.student().getName();
        log.add(new Notification("PUSH", to, e.message()));
    }
}
