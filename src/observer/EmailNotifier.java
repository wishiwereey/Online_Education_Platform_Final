package observer;
public class EmailNotifier implements LearningObserver {
    private final NotificationLog log;
    public EmailNotifier(NotificationLog log) {
        this.log = log;
    }
    @Override
    public void update(LearningEvent e) {
        if (e.isBroadcast()) return;
        log.add(new Notification("EMAIL", e.student().getEmail(), e.message()));
    }
}
