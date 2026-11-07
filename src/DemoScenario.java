import builder.Course;
import decorator.*;
import factory.*;
import facade.LearningPlatformFacade;
import model.*;
import observer.EventType;
import service.Enrollment;
import strategy.*;
import java.time.LocalDate;
import java.util.List;
final class DemoScenario {
    private DemoScenario() { }
    static void run() {
        LearningPlatformFacade platform = new LearningPlatformFacade();
        platform.setToday(LocalDate.of(2026, 11, 2));
        platform.notifications().setPrinter(n -> Console.info("  >> " + n));
        Console.title("ONLINE EDUCATION PLATFORM - design patterns demo");
        Console.title("1. BUILDER - assembling courses step by step");
        Console.step("Publishing courses (each publish also notifies observers)");
        DemoData.seedCourses(platform);
        DemoData.seedStudents(platform);
        Console.step("Catalog");
        for (Course c : platform.courses()) {
            Console.bullet(c);
            Console.info("       modules: " + c.getModules() + ", quizzes: " + c.getQuizzes().size()
                    + ", tags: " + c.getTags());
        }
        Console.step("Builder validation: a course without modules is rejected");
        try {
            Course.builder("BAD-1", "Empty course").price(10).build();
        } catch (IllegalStateException e) {
            Console.info("Rejected: " + e.getMessage());
        }
        Console.title("2. DECORATOR - stacking paid add-ons on course access");
        Console.step("Price quotes for DP-201 with different add-on combinations");
        quote(platform, "DP-201");
        quote(platform, "DP-201", AddOn.CERTIFICATE);
        quote(platform, "DP-201", AddOn.CERTIFICATE, AddOn.MENTOR);
        quote(platform, "DP-201", AddOn.CERTIFICATE, AddOn.MENTOR, AddOn.OFFLINE, AddOn.SUBTITLES_RU);
        Console.title("3. FACADE - one call does the whole enrollment");
        Console.step("platform.enroll(...) = find course + decorate + pay + register + notify");
        enroll(platform, "S1", "DP-201", AddOn.CERTIFICATE, AddOn.SUBTITLES_RU);
        enroll(platform, "S1", "JAVA-101");
        enroll(platform, "S2", "ML-301", AddOn.MENTOR);
        enroll(platform, "S3", "UX-110", AddOn.OFFLINE);
        Console.step("Trying to enroll twice");
        try {
            platform.enroll("S1", "JAVA-101");
        } catch (IllegalStateException e) {
            Console.info("Rejected: " + e.getMessage());
        }
        Console.title("4. FACTORY METHOD - creating different assignment types");
        Console.step("Instructor creates assignments through creators (no 'new QuizAssignment')");
        Assignment quiz = platform.createAssignment(new QuizAssignmentFactory(10), "DP-201", "Creational patterns quiz");
        Assignment coding = platform.createAssignment(new CodingAssignmentFactory("Java", 6), "DP-201", "Implement a Decorator");
        Assignment essay = platform.createAssignment(new EssayAssignmentFactory(400), "UX-110", "Persona research essay");
        Assignment project = platform.createAssignment(new ProjectAssignmentFactory(3), "ML-301", "Image classifier");
        Assignment fromText = platform.createAssignment("quiz", "JAVA-101", "Loops and conditions");
        Console.step("Created assignments");
        for (Assignment a : List.of(quiz, coding, essay, project, fromText)) {
            Console.bullet(a + " (" + a.getClass().getSimpleName() + ")");
            Console.info("       " + a.getInstructions());
        }
        Console.title("5. OBSERVER - live notifications about learning events");
        Console.step("Students submit work");
        Submission s1 = platform.submit("S1", quiz.getId(), "answers: B, A, C, D, ...");
        Submission s2 = platform.submit("S1", coding.getId(), "class MentorDecorator extends ...");
        Submission s3 = platform.submit("S3", essay.getId(), "Our persona is a busy student ...");
        for (Submission s : List.of(s1, s2, s3)) {
            Console.bullet(s.getStudent().getName() + " submitted '" + s.getAssignment().getTitle() + "'");
        }
        Console.info("(ASSIGNMENT_SUBMITTED is heard only by the dashboard observer: "
                + platform.dashboard().count(EventType.ASSIGNMENT_SUBMITTED) + " so far)");
        Console.step("Instructor grades - e-mail + push observers react, dashboard counts");
        platform.grade(s1, 18, "Great job!");
        platform.grade(s3, 72, "Good research, add more interviews.");
        platform.grade(s2, 54, "All main tests pass.");
        Console.step("Deadline reminders for the next 30 days (only for work not submitted yet)");
        int sent = platform.sendDeadlineReminders(30);
        Console.info("Reminders sent: " + sent);
        Console.step("Instructor dashboard (an observer subscribed to every event)");
        for (EventType t : EventType.values()) {
            Console.info(String.format("%-22s %d", t, platform.dashboard().count(t)));
        }
        Console.info("Graded assignments of Aruzhan (ProgressTracker observer): " + platform.gradedAssignments("S1"));
        Console.title("6. STRATEGY - switching recommendation algorithms at runtime");
        List<RecommendationStrategy> strategies = List.of(
                new PopularityStrategy(), new InterestBasedStrategy(),
                new LevelBasedStrategy(), new BudgetStrategy(40));
        for (String sid : List.of("S1", "S2")) {
            Console.step("Recommendations for " + platform.student(sid).getName()
                    + " (level " + platform.student(sid).getLevel()
                    + ", interests " + platform.student(sid).getInterests() + ")");
            for (RecommendationStrategy strategy : strategies) {
                platform.setRecommendationStrategy(strategy);
                List<String> titles = platform.recommend(sid, 3).stream().map(Course::getTitle).toList();
                Console.info(String.format("%-20s -> %s", strategy.name(), titles.isEmpty() ? "(nothing fits)" : titles));
            }
        }
        Console.title("SUMMARY");
        Console.info("Courses: " + platform.courses().size() + ", students: " + platform.students().size());
        Console.info("Notifications delivered: " + platform.notifications().all().size());
        Console.info(String.format("Revenue: $%.2f", platform.totalRevenue()));
        Console.info("Aruzhan completed: " + platform.student("S1").getCompletedCourseIds()
                + ", level now " + platform.student("S1").getLevel());
        Console.OUT.println();
    }
    private static void quote(LearningPlatformFacade p, String courseId, AddOn... addOns) {
        CourseAccess a = p.quote(courseId, addOns);
        Console.bullet(String.format("$%7.2f  %s", a.getPrice(), a.getDescription()));
    }
    private static void enroll(LearningPlatformFacade p, String sid, String courseId, AddOn... addOns) {
        Enrollment e = p.enroll(sid, courseId, addOns);
        Console.bullet(String.format("%s -> %s, paid $%.2f, features %s",
                e.student().getName(), courseId, e.amountPaid(), e.access().getFeatures()));
    }
}
