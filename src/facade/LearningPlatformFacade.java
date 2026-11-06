package facade;
import builder.Course;
import decorator.*;
import factory.*;
import model.*;
import observer.*;
import service.*;
import strategy.*;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
public class LearningPlatformFacade {
    private final NotificationCenter events = new NotificationCenter();
    private final NotificationLog notificationLog = new NotificationLog();
    private final InstructorDashboard dashboard = new InstructorDashboard();
    private final ProgressTracker progress = new ProgressTracker();
    private final CourseCatalog catalog = new CourseCatalog(events);
    private final EnrollmentService enrollments = new EnrollmentService(events);
    private final PaymentService payments = new PaymentService();
    private final AssignmentService assignments = new AssignmentService(events, enrollments);
    private final RecommendationEngine recommender = new RecommendationEngine(new PopularityStrategy());
    private final Map<String, Student> students = new LinkedHashMap<>();
    private LocalDate today = LocalDate.now();
    public LearningPlatformFacade() {
        events.subscribe(new EmailNotifier(notificationLog),
                EventType.ENROLLED, EventType.ASSIGNMENT_GRADED,
                EventType.DEADLINE_REMINDER, EventType.COURSE_COMPLETED);
        events.subscribe(new PushNotifier(notificationLog),
                EventType.COURSE_PUBLISHED, EventType.ASSIGNMENT_PUBLISHED,
                EventType.ASSIGNMENT_GRADED, EventType.DEADLINE_REMINDER, EventType.COURSE_COMPLETED);
        events.subscribe(dashboard); // all event types
        events.subscribe(progress, EventType.ASSIGNMENT_GRADED);
    }
    public Student registerStudent(String id, String name, String email, Level level, String... interests) {
        if (students.containsKey(id)) throw new IllegalArgumentException("Student id already used: " + id);
        Student s = new Student(id, name, email, level, List.of(interests));
        students.put(id, s);
        return s;
    }
    public Student student(String id) {
        Student s = students.get(id);
        if (s == null) throw new IllegalArgumentException("No such student: " + id);
        return s;
    }
    public List<Student> students() {
        return List.copyOf(students.values());
    }
    public Course publishCourse(Course course) {
        catalog.publish(course);
        return course;
    }
    public List<Course> courses() {
        return catalog.all();
    }
    public Course course(String id) {
        return catalog.get(id);
    }
    public CourseAccess quote(String courseId, AddOn... addOns) {
        CourseAccess access = new BasicCourseAccess(catalog.get(courseId));
        for (AddOn addOn : addOns) {
            access = addOn.applyTo(access);
        }
        return access;
    }
    public Enrollment enroll(String studentId, String courseId, AddOn... addOns) {
        Student s = student(studentId);
        CourseAccess access = quote(courseId, addOns);
        if (enrollments.isEnrolled(s, courseId)) {
            throw new IllegalStateException(s.getName() + " is already enrolled in " + courseId);
        }
        PaymentService.Receipt receipt = payments.charge(s.getId(), access.getDescription(), access.getPrice());
        return enrollments.enroll(s, access, receipt.amount());
    }
    public List<Enrollment> enrollmentsOf(String studentId) {
        return enrollments.of(student(studentId));
    }
    public Assignment createAssignment(AssignmentFactory factory, String courseId, String title) {
        catalog.get(courseId); // validate course exists
        return assignments.create(factory, title, courseId, today);
    }
    public Assignment createAssignment(String type, String courseId, String title) {
        return createAssignment(AssignmentFactory.forType(type), courseId, title);
    }
    public List<Assignment> assignmentsOf(String courseId) {
        return assignments.forCourse(courseId);
    }
    public Submission submit(String studentId, String assignmentId, String content) {
        return assignments.submit(student(studentId), assignmentId, content);
    }
    public List<Submission> pendingSubmissions() {
        return assignments.ungraded(); }
    public void grade(Submission submission, int score, String feedback) {
        assignments.grade(submission, score, feedback);
        Student s = submission.getStudent();
        String courseId = submission.getAssignment().getCourseId();
        if (!s.getCompletedCourseIds().contains(courseId) && assignments.hasPassedCourse(s, courseId)) {
            s.markCompleted(courseId);
            Course c = catalog.get(courseId);
            if (c.getLevel().ordinal() >= s.getLevel().ordinal()) s.setLevel(s.getLevel().next());
            events.publish(new LearningEvent(EventType.COURSE_COMPLETED, s, courseId,
                    "Congratulations! You completed '" + c.getTitle() + "'. Your level is now " + s.getLevel()));
        }
    }
    public int sendDeadlineReminders(int daysAhead) {
        return assignments.remindDeadlines(today, daysAhead);
    }
    public void setRecommendationStrategy(RecommendationStrategy strategy) {
        recommender.setStrategy(strategy);
    }
    public RecommendationStrategy recommendationStrategy() {
        return recommender.getStrategy();
    }
    public List<Course> recommend(String studentId, int limit) {
        Student s = student(studentId);
        return recommender.recommend(s, catalog.all(), enrollments.courseIdsOf(s), limit);
    }
    public NotificationCenter events() {
        return events;
    }
    public NotificationLog notifications() {
        return notificationLog;
    }
    public InstructorDashboard dashboard() {
        return dashboard;
    }
    public int gradedAssignments(String studentId) {
        return progress.gradedAssignments(studentId);
    }
    public double totalRevenue() {
        return payments.totalRevenue();
    }
    public LocalDate today() {
        return today;
    }
    public void setToday(LocalDate today) {
        this.today = today;
    }
}
