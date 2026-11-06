package service;

import decorator.CourseAccess;
import model.Student;
import observer.EventType;
import observer.LearningEvent;
import observer.NotificationCenter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
public class EnrollmentService {
    private final List<Enrollment> enrollments = new ArrayList<>();
    private final NotificationCenter events;
    public EnrollmentService(NotificationCenter events) {
        this.events = events;
    }
    public boolean isEnrolled(Student s, String courseId) {
        return enrollments.stream().anyMatch(e -> e.student().equals(s) && e.courseId().equals(courseId));
    }
    public long countFor(String courseId) {
        return enrollments.stream().filter(e -> e.courseId().equals(courseId)).count();
    }
    public Enrollment enroll(Student s, CourseAccess access, double paid) {
        String courseId = access.getCourse().getId();
        if (isEnrolled(s, courseId)) {
            throw new IllegalStateException(s.getName() + " is already enrolled in " + courseId);
        }
        if (countFor(courseId) >= access.getCourse().getMaxStudents()) {
            throw new IllegalStateException("Course " + courseId + " is full");
        }
        Enrollment e = new Enrollment(s, access, paid);
        enrollments.add(e);
        access.getCourse().incrementPopularity();
        events.publish(new LearningEvent(EventType.ENROLLED, s, courseId,
                "You are enrolled in '" + access.getDescription() + "'"));
        return e;
    }
    public List<Enrollment> of(Student s) {
        return enrollments.stream().filter(e -> e.student().equals(s)).toList();
    }
    public List<Student> studentsOf(String courseId) {
        return enrollments.stream().filter(e -> e.courseId().equals(courseId)).map(Enrollment::student).toList();
    }
    public Set<String> courseIdsOf(Student s) {
        return of(s).stream().map(Enrollment::courseId).collect(Collectors.toSet());
    }
}
