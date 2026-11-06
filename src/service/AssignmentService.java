package service;
import factory.Assignment;
import factory.AssignmentFactory;
import model.Student;
import model.Submission;
import observer.EventType;
import observer.LearningEvent;
import observer.NotificationCenter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
public class AssignmentService {
    private final Map<String, Assignment> assignments = new LinkedHashMap<>();
    private final List<Submission> submissions = new ArrayList<>();
    private final NotificationCenter events;
    private final EnrollmentService enrollments;
    public AssignmentService(NotificationCenter events, EnrollmentService enrollments) {
        this.events = events;
        this.enrollments = enrollments;
    }
    public Assignment create(AssignmentFactory factory, String title, String courseId, LocalDate start) {
        Assignment a = factory.publish(title, courseId, start);
        assignments.put(a.getId(), a);
        for (Student s : enrollments.studentsOf(courseId)) {
            events.publish(new LearningEvent(EventType.ASSIGNMENT_PUBLISHED, s, courseId,
                    "New " + a.getType().toLowerCase() + " '" + a.getTitle() + "' due " + a.getDeadline()));
        }
        return a;
    }
    public Assignment get(String id) {
        Assignment a = assignments.get(id);
        if (a == null) throw new IllegalArgumentException("No such assignment: " + id);
        return a;
    }
    public List<Assignment> forCourse(String courseId) {
        return assignments.values().stream().filter(a -> a.getCourseId().equals(courseId)).toList();
    }
    public Submission submit(Student s, String assignmentId, String content) {
        Assignment a = get(assignmentId);
        if (!enrollments.isEnrolled(s, a.getCourseId())) {
            throw new IllegalStateException(s.getName() + " is not enrolled in " + a.getCourseId());
        }
        Submission sub = new Submission(s, a, content);
        submissions.add(sub);
        events.publish(new LearningEvent(EventType.ASSIGNMENT_SUBMITTED, s, a.getCourseId(),
                "Submission received for '" + a.getTitle() + "'"));
        return sub;
    }
    public void grade(Submission sub, int score, String feedback) {
        sub.grade(score, feedback);
        Assignment a = sub.getAssignment();
        events.publish(new LearningEvent(EventType.ASSIGNMENT_GRADED, sub.getStudent(), a.getCourseId(),
                String.format("'%s' graded: %d/%d. %s", a.getTitle(), score, a.getMaxScore(), feedback)));
    }
    public List<Submission> submissionsOf(Student s) {
        return submissions.stream().filter(x -> x.getStudent().equals(s)).toList();
    }

    public List<Submission> ungraded() {

        return submissions.stream().filter(x -> !x.isGraded()).toList();
    }
    public int remindDeadlines(LocalDate today, int days) {
        int sent = 0;
        for (Assignment a : assignments.values()) {
            if (a.getDeadline().isBefore(today) || a.getDeadline().isAfter(today.plusDays(days))) continue;
            for (Student s : enrollments.studentsOf(a.getCourseId())) {
                boolean done = submissions.stream()
                        .anyMatch(x -> x.getStudent().equals(s) && x.getAssignment().equals(a));
                if (!done) {
                    events.publish(new LearningEvent(EventType.DEADLINE_REMINDER, s, a.getCourseId(),
                            "Reminder: '" + a.getTitle() + "' is due " + a.getDeadline()));
                    sent++;
                }
            }
        }
        return sent;
    }
    public boolean hasPassedCourse(Student s, String courseId) {
        List<Assignment> list = forCourse(courseId);
        if (list.isEmpty()) return false;
        return list.stream().allMatch(a -> submissions.stream().anyMatch(x ->
                x.getStudent().equals(s) && x.getAssignment().equals(a)
                        && x.isGraded() && x.getScore() * 2 >= a.getMaxScore()));
    }
}