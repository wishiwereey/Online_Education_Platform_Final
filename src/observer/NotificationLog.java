package observer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
public class NotificationLog {
    private final List<Notification> all = new ArrayList<>();
    private Consumer<Notification> printer = n -> { };
    public void setPrinter(Consumer<Notification> printer) {
        this.printer = printer;
    }
    void add(Notification n) {
        all.add(n);
        printer.accept(n);
    }
    public List<Notification> all() {
        return List.copyOf(all);
    }
    public List<Notification> forRecipient(String recipient) {
        return all.stream().filter(n -> n.recipient().equals(recipient)).toList();
    }
    public void clear() {
        all.clear();
    }
}
