package model;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
public class Student {
    private final String id;
    private final String name;
    private final String email;
    private final Set<String> interests;
    private Level level;
    private final Set<String> completedCourseIds = new LinkedHashSet<>();
    public Student(String id, String name, String email, Level level, List<String> interests) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.level = level;
        this.interests = new LinkedHashSet<>(interests);
    }
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public Level getLevel() {
        return level;
    }
    public void setLevel(Level level) {
        this.level = level;
    }
    public Set<String> getInterests() {
        return Collections.unmodifiableSet(interests);
    }
    public Set<String> getCompletedCourseIds() {
        return Collections.unmodifiableSet(completedCourseIds);
    }
    public void markCompleted(String courseId) {
        completedCourseIds.add(courseId);
    }
    @Override
    public String toString() {
        return name + " <" + email + ">";
    }
}
