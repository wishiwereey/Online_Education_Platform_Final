package model;
import java.util.List;
public final class Module {
    private final String title;
    private final List<String> lessons;
    public Module(String title, List<String> lessons) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Module title is required");
        }
        this.title = title;
        this.lessons = List.copyOf(lessons);
    }
    public String getTitle() {
        return title;
    }
    public List<String> getLessons() {
        return lessons;
    }
    @Override
    public String toString() {
        return title + " (" + lessons.size() + " lessons)";
    }
}
