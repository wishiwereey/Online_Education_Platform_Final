package service;

import builder.Course;
import observer.EventType;
import observer.LearningEvent;
import observer.NotificationCenter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
public class CourseCatalog {
    private final Map<String, Course> courses = new LinkedHashMap<>();
    private final NotificationCenter events;
    public CourseCatalog(NotificationCenter events) {
        this.events = events;
    }
    public void publish(Course course) {
        if (courses.containsKey(course.getId())) {
            throw new IllegalArgumentException("Course id already used: " + course.getId());
        }
        courses.put(course.getId(), course);
        events.publish(new LearningEvent(EventType.COURSE_PUBLISHED, null, course.getId(),
                "New course available: " + course.getTitle()));
    }
    public Optional<Course> find(String id) {
        return Optional.ofNullable(courses.get(id));
    }
    public Course get(String id) {
        return find(id).orElseThrow(() -> new IllegalArgumentException("No such course: " + id));
    }
    public List<Course> all() {
        return new ArrayList<>(courses.values());
    }
}
