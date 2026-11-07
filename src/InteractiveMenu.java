import builder.Course;
import decorator.AddOn;
import decorator.CourseAccess;
import factory.Assignment;
import facade.LearningPlatformFacade;
import model.Student;
import model.Submission;
import observer.EventType;
import observer.Notification;
import service.Enrollment;
import strategy.BudgetStrategy;
import strategy.InterestBasedStrategy;
import strategy.LevelBasedStrategy;
import strategy.PopularityStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
final class InteractiveMenu {
    private final LearningPlatformFacade platform = new LearningPlatformFacade();
    private final Scanner in = new Scanner(System.in);
    InteractiveMenu() {
        DemoData.seedCourses(platform);
        DemoData.seedStudents(platform);
        platform.notifications().clear(); // don't show seeding notifications
        platform.notifications().setPrinter(n -> Console.info(">> " + n));
    }
    void run() {
        Console.title("ONLINE EDUCATION PLATFORM - interactive mode");
        while (true) {
            Console.OUT.println();
            Console.OUT.println(" 1) List courses              6) Grade pending submissions");
            Console.OUT.println(" 2) Build a new course        7) Get recommendations");
            Console.OUT.println(" 3) Enroll with add-ons       8) Send deadline reminders");
            Console.OUT.println(" 4) Create assignment         9) Show notifications & dashboard");
            Console.OUT.println(" 5) Submit assignment         0) Exit");
            String choice = ask("Choose");
            try {
                switch (choice) {
                    case "1" -> listCourses();
                    case "2" -> buildCourse();
                    case "3" -> enroll();
                    case "4" -> createAssignment();
                    case "5" -> submit();
                    case "6" -> grade();
                    case "7" -> recommend();
                    case "8" -> Console.info("Reminders sent: " + platform.sendDeadlineReminders(30));
                    case "9" -> showNotifications();
                    case "" -> { }
                    case "0", "q" -> { return; }
                    default -> Console.info("Unknown option");
                }
            } catch (RuntimeException e) {
                Console.info("Error: " + e.getMessage());
            }
        }
    }
    private String ask(String prompt) {
        Console.OUT.print(prompt + ": ");
        Console.OUT.flush();
        return in.hasNextLine() ? in.nextLine().trim() : "0";
    }
    private Student pickStudent() {
        for (Student s : platform.students()) Console.info(s.getId() + " - " + s.getName() + " (" + s.getLevel() + ")");
        return platform.student(ask("Student id").toUpperCase());
    }
    private void listCourses() {
        for (Course c : platform.courses()) {
            Console.bullet(c + " popularity " + c.getPopularity() + ", tags " + c.getTags());
        }
    }
    private void buildCourse() {
        Course.Builder b = Course.builder(ask("Course id").toUpperCase(), ask("Title"))
                .instructor(ask("Instructor"))
                .price(Double.parseDouble(ask("Price")))
                .level(model.Level.valueOf(ask("Level (BEGINNER/INTERMEDIATE/ADVANCED)").toUpperCase()));
        String tags = ask("Tags (comma separated)");
        if (!tags.isBlank()) b.tags(tags.split("\\s*,\\s*"));
        while (true) {
            String module = ask("Module title (empty to finish)");
            if (module.isBlank()) break;
            b.addModule(module, ask("Lessons (comma separated)").split("\\s*,\\s*"));
        }
        Console.info("Published: " + platform.publishCourse(b.build()));
    }
    private void enroll() {
        Student s = pickStudent();
        listCourses();
        String courseId = ask("Course id").toUpperCase();
        AddOn[] all = AddOn.values();
        for (int i = 0; i < all.length; i++) {
            Console.info((i + 1) + ") " + all[i].getLabel() + String.format(" +$%.2f", all[i].getPrice()));
        }
        List<AddOn> chosen = new ArrayList<>();
        for (String part : ask("Add-ons (e.g. 1,3 or empty)").split("[,\\s]+")) {
            if (!part.isBlank()) chosen.add(all[Integer.parseInt(part) - 1]);
        }
        AddOn[] addOns = chosen.toArray(AddOn[]::new);
        CourseAccess q = platform.quote(courseId, addOns);
        Console.info(String.format("Quote: %s = $%.2f", q.getDescription(), q.getPrice()));
        if (ask("Confirm (y/n)").equalsIgnoreCase("y")) {
            Enrollment e = platform.enroll(s.getId(), courseId, addOns);
            Console.info(String.format("Enrolled, paid $%.2f, features %s", e.amountPaid(), e.access().getFeatures()));
        }
    }
    private void createAssignment() {
        listCourses();
        String courseId = ask("Course id").toUpperCase();
        String type = ask("Type (quiz/essay/coding/project)");
        Assignment a = platform.createAssignment(type, courseId, ask("Title"));
        Console.info("Created " + a);
        Console.info(a.getInstructions());
    }
    private void submit() {
        Student s = pickStudent();
        for (Enrollment e : platform.enrollmentsOf(s.getId())) {
            for (Assignment a : platform.assignmentsOf(e.courseId())) Console.bullet(a);
        }
        platform.submit(s.getId(), ask("Assignment id").toUpperCase(), ask("Your answer"));
    }
    private void grade() {
        List<Submission> pending = platform.pendingSubmissions();
        if (pending.isEmpty()) { Console.info("Nothing to grade"); return; }
        for (int i = 0; i < pending.size(); i++) {
            Submission sub = pending.get(i);
            Console.info((i + 1) + ") " + sub.getStudent().getName() + " - " + sub.getAssignment());
        }
        Submission sub = pending.get(Integer.parseInt(ask("Number")) - 1);
        platform.grade(sub, Integer.parseInt(ask("Score (max " + sub.getAssignment().getMaxScore() + ")")),
                ask("Feedback"));
    }
    private void recommend() {
        Student s = pickStudent();
        Console.info("1) Most popular  2) By interests  3) Next step by level  4) Budget");
        switch (ask("Strategy")) {
            case "2" -> platform.setRecommendationStrategy(new InterestBasedStrategy());
            case "3" -> platform.setRecommendationStrategy(new LevelBasedStrategy());
            case "4" -> platform.setRecommendationStrategy(new BudgetStrategy(Double.parseDouble(ask("Max price"))));
            default -> platform.setRecommendationStrategy(new PopularityStrategy());
        }
        Console.info("Strategy: " + platform.recommendationStrategy().name());
        List<Course> list = platform.recommend(s.getId(), 3);
        if (list.isEmpty()) Console.info("(nothing fits)");
        list.forEach(Console::bullet);
    }
    private void showNotifications() {
        for (Notification n : platform.notifications().all()) Console.bullet(n);
        Console.step("Dashboard");
        for (EventType t : EventType.values()) Console.info(String.format("%-22s %d", t, platform.dashboard().count(t)));
    }
}