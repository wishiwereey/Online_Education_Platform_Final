package observer;
public record Notification(String channel, String recipient, String text) {
    @Override
    public String toString() {
        return "[" + channel + " -> " + recipient + "] " + text;
    }
}
