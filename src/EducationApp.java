
import builder.Course;
import decorator.AddOn;
import decorator.CourseAccess;
import facade.LearningPlatformFacade;
import factory.Assignment;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Level;
import model.Student;
import model.Submission;
import observer.Notification;
import strategy.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class EducationApp extends Application {
    private static final String NAVY = "#111827";
    private static final String BLUE = "#4F46E5";
    private static final String BG = "#F3F5FA";
    private static final String TEXT = "#182230";
    private static final String MUTED = "#667085";
    private final LearningPlatformFacade platform = new LearningPlatformFacade();
    private Stage stage;
    private BorderPane shell;
    private VBox sidebar;
    private final List<Button> navButtons = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        DemoData.seedCourses(platform);
        DemoData.seedStudents(platform);
        shell = new BorderPane();
        shell.setStyle("-fx-background-color: " + BG + ";");
        buildSidebar();
        shell.setLeft(sidebar);
        Scene scene = new Scene(shell, 1120, 720);
        stage.setScene(scene);
        stage.setMinWidth(950);
        stage.setMinHeight(650);
        stage.setTitle("EduFlow | Online Education Platform");
        showDashboard();
        stage.show();
    }

    private void buildSidebar() {
        sidebar = new VBox(8);
        sidebar.setPrefWidth(245);
        sidebar.setPadding(new Insets(28, 14, 24, 14));
        sidebar.setStyle("-fx-background-color: " + NAVY + ";");

        Label logo = new Label("◆  EduFlow");
        logo.setStyle("-fx-text-fill: white; -fx-font-size: 25px; -fx-font-weight: bold;");
        Label subtitle = new Label("LEARNING MANAGEMENT");
        subtitle.setStyle("-fx-text-fill: #98A2B3; -fx-font-size: 10px; -fx-font-weight: bold;");
        VBox branding = new VBox(7, logo, subtitle);
        branding.setPadding(new Insets(0, 12, 24, 12));
        sidebar.getChildren().add(branding);

        addNav("⌂  Dashboard", this::showDashboard);
        addNav("◉  Register", this::showMyLearning);
        addNav("▤  Course Catalog", this::showCatalog);
        addNav("＋  Create Course", this::showCreateCourse);
        addNav("▣  Create Assignment", this::showCreateAssignment);
        addNav("↗  Submit Assignment", this::showSubmitAssignment);
        addNav("✓  Grade Assignments", this::showGradeAssignments);
        addNav("✦  Recommendations", this::showRecommendations);
        addNav("◇  Course Add-ons", this::showCourseAddOns);
        addNav("♧  Notifications", this::showNotifications);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        Label footer = new Label("Online Education Platform");
        footer.setStyle("-fx-text-fill: #98A2B3; -fx-font-size: 11px;");
        footer.setPadding(new Insets(15, 12, 0, 12));
        sidebar.getChildren().addAll(spacer, footer);
    }

    private void addNav(String text, Runnable action) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPrefHeight(42);
        button.setStyle(navStyle(false));
        button.setOnAction(e -> {
            selectNav(button);
            action.run();
        });
        navButtons.add(button);
        sidebar.getChildren().add(button);
    }

    private String navStyle(boolean active) {
        return "-fx-background-color: " + (active ? BLUE : "transparent") + ";"
                + "-fx-text-fill: " + (active ? "white" : "#CBD5E1") + ";"
                + "-fx-font-size: 13px; -fx-font-weight: bold;"
                + "-fx-background-radius: 10; -fx-padding: 10 14; -fx-cursor: hand;";
    }

    private void selectNav(Button selected) {
        for (Button button : navButtons) button.setStyle(navStyle(button == selected));
    }

    private void activate(int index) {
        if (index >= 0 && index < navButtons.size()) selectNav(navButtons.get(index));
    }

    private Label label(String text, int size, boolean bold) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: " + TEXT + "; -fx-font-size: " + size + "px;"
                + (bold ? "-fx-font-weight: bold;" : ""));
        label.setWrapText(true);
        return label;
    }

    private Label muted(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: " + MUTED + "; -fx-font-size: 13px;");
        label.setWrapText(true);
        return label;
    }

    private VBox card(Node... nodes) {
        VBox card = new VBox(12, nodes);
        card.setPadding(new Insets(22));
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 16;"
                + "-fx-border-color: #E7EAF0; -fx-border-radius: 16;");
        return card;
    }

    private Button primary(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + BLUE + "; -fx-text-fill: white;"
                + "-fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 9;"
                + "-fx-padding: 12 19; -fx-cursor: hand;");
        return button;
    }

    private Button secondary(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #EEF2FF; -fx-text-fill: #4338CA;"
                + "-fx-font-weight: bold; -fx-background-radius: 9;"
                + "-fx-padding: 11 18; -fx-cursor: hand;");
        return button;
    }

    private TextField field(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setPrefHeight(40);
        return field;
    }

    private <T> ComboBox<T> combo(String prompt, List<T> values) {
        ComboBox<T> box = new ComboBox<>();
        box.getItems().addAll(values);
        box.setPromptText(prompt);
        box.setMaxWidth(Double.MAX_VALUE);
        box.setPrefHeight(40);
        return box;
    }

    private VBox formRow(String name, Node control) {
        return new VBox(7, label(name, 13, true), control);
    }

    private void page(String title, String description, Node... contents) {
        VBox body = new VBox(18);
        body.setPadding(new Insets(32, 38, 42, 38));
        body.getChildren().addAll(label(title, 28, true), muted(description));
        body.getChildren().addAll(contents);
        ScrollPane scroll = new ScrollPane(body);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: " + BG + "; -fx-background-color: transparent;"
                + "-fx-border-color: transparent;");
        shell.setCenter(scroll);
    }

    private void message(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("EduFlow");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    private void showDashboard() {
        activate(0);
        VBox courses = card(label("Published courses", 15, true),
                label(String.valueOf(platform.courses().size()), 32, true), muted("Available in the catalog"));
        VBox students = card(label("Registered students", 15, true),
                label(String.valueOf(platform.students().size()), 32, true), muted("Learning on EduFlow"));
        VBox pending = card(label("Pending reviews", 15, true),
                label(String.valueOf(platform.pendingSubmissions().size()), 32, true), muted("Assignments to grade"));
        HBox stats = new HBox(15, courses, students, pending);
        HBox.setHgrow(courses, Priority.ALWAYS);
        HBox.setHgrow(students, Priority.ALWAYS);
        HBox.setHgrow(pending, Priority.ALWAYS);
        courses.setPrefWidth(240);
        students.setPrefWidth(240);
        pending.setPrefWidth(240);

        Button browse = primary("Explore Courses");
        browse.setOnAction(e -> showCatalog());
        Button register = secondary("Register Student");
        register.setOnAction(e -> showMyLearning());
        HBox actions = new HBox(12, browse, register);
        VBox welcome = card(label("Welcome to EduFlow ✨", 23, true),
                muted("Create courses, manage assignments and discover personalized learning paths."), actions);
        welcome.setStyle("-fx-background-color: #E9E8FF; -fx-background-radius: 16;");

        page("Dashboard", "Your education platform at a glance", welcome, stats);
    }

    private void showCatalog() {
        activate(2);
        VBox list = new VBox(14);
        for (Course course : platform.courses()) {
            Label title = label(course.getTitle(), 19, true);
            Label level = muted("LEVEL  " + course.getLevel());
            Label details = muted("Instructor: " + course.getInstructor()
                    + "     •     " + course.getLessonCount() + " lessons");
            Label price = label(String.format("$%.2f", course.getBasePrice()), 20, true);
            list.getChildren().add(card(title, level, details, price));
        }
        page("Course Catalog", "Browse courses created with the Builder pattern", list);
    }

    private void showCreateCourse() {
        activate(3);
        TextField title = field("e.g. Web Development Basics");
        TextField instructor = field("Instructor name");
        ComboBox<Level> level = combo("Select level", Arrays.asList(Level.values()));
        level.setValue(Level.BEGINNER);
        TextField price = field("e.g. 49.99");
        TextField module = field("e.g. Introduction");
        TextField lessons = field("Variables, Functions, Classes");
        TextField quiz = field("Optional quiz title");
        Button save = primary("Create Course");
        save.setOnAction(e -> {
            try {
                String name = title.getText().trim();
                String teacher = instructor.getText().trim();
                String moduleName = module.getText().trim();
                if (name.isEmpty() || teacher.isEmpty() || moduleName.isEmpty()) {
                    message("Please complete all required fields.");
                    return;
                }
                double value = Double.parseDouble(price.getText().trim());
                if (!Double.isFinite(value) || value < 0) {
                    message("Please enter a valid non-negative price.");
                    return;
                }
                String[] lessonNames = Arrays.stream(lessons.getText().split(","))
                        .map(String::trim).filter(s -> !s.isEmpty()).toArray(String[]::new);
                if (lessonNames.length == 0) {
                    message("Please enter at least one lesson.");
                    return;
                }
                Course.Builder builder = Course.builder(UUID.randomUUID().toString().substring(0, 8), name)
                        .instructor(teacher).level(level.getValue()).price(value)
                        .addModule(moduleName, lessonNames);
                if (!quiz.getText().isBlank()) builder.addQuiz(quiz.getText().trim(), 10, 70);
                platform.publishCourse(builder.build());
                message("Course created successfully!");
                showCatalog();
            } catch (NumberFormatException ex) {
                message("Please enter a valid price.");
            } catch (IllegalArgumentException | IllegalStateException ex) {
                message("Error: " + ex.getMessage());
            }
        });
        page("Create Course", "Build a customized course with modules and quizzes",
                card(formRow("Course title", title), formRow("Instructor", instructor),
                        formRow("Difficulty level", level), formRow("Price ($)", price),
                        formRow("Module title", module), formRow("Lessons (comma-separated)", lessons),
                        formRow("Quiz (optional)", quiz), save));
    }

    private void showCreateAssignment() {
        activate(4);
        ComboBox<Course> course = combo("Select course", platform.courses());
        ComboBox<String> type = combo("Assignment type", List.of("quiz", "essay", "coding", "project"));
        type.setValue("quiz");
        TextField title = field("Assignment title");
        Button create = primary("Create Assignment");
        create.setOnAction(e -> {
            try {
                if (course.getValue() == null || title.getText().isBlank()) {
                    message("Select a course and enter an assignment title.");
                    return;
                }
                Assignment result = platform.createAssignment(type.getValue(),
                        course.getValue().getId(), title.getText().trim());
                message("Assignment created!\nTitle: " + result.getTitle()
                        + "\nDeadline: " + result.getDeadline());
                showDashboard();
            } catch (IllegalArgumentException | IllegalStateException ex) {
                message("Error: " + ex.getMessage());
            }
        });
        page("Create Assignment", "Factory Method creates different types of assignments",
                card(formRow("Course", course), formRow("Assignment type", type),
                        formRow("Assignment title", title), create));
    }

    private void showMyLearning() {
        activate(1);
        TextField name = field("Full name");
        TextField email = field("Email address");
        TextField interests = field("java, python, programming");
        Button register = primary("Register Student");
        ComboBox<Student> student = combo("Select student", platform.students());
        ComboBox<Course> course = combo("Select course", platform.courses());
        Button enroll = primary("Enroll in Course");
        VBox myCourses = new VBox(10);
        Runnable refresh = () -> {
            myCourses.getChildren().clear();
            if (student.getValue() == null) {
                myCourses.getChildren().add(muted("Select a student to see enrolled courses."));
                return;
            }
            var enrollments = platform.enrollmentsOf(student.getValue().getId());
            if (enrollments.isEmpty()) myCourses.getChildren().add(muted("No enrolled courses yet."));
            for (var item : enrollments) {
                Course c = item.access().getCourse();
                myCourses.getChildren().add(card(label(c.getTitle(), 16, true),
                        muted("Level: " + c.getLevel()),
                        label(String.format("Paid: $%.2f", item.amountPaid()), 14, true)));
            }
        };
        register.setOnAction(e -> {
            try {
                if (name.getText().isBlank() || email.getText().isBlank()) {
                    message("Please enter name and email.");
                    return;
                }
                String[] tags = Arrays.stream(interests.getText().split(","))
                        .map(String::trim).map(String::toLowerCase)
                        .filter(s -> !s.isEmpty()).toArray(String[]::new);
                Student created = platform.registerStudent(UUID.randomUUID().toString().substring(0, 8),
                        name.getText().trim(), email.getText().trim(), Level.BEGINNER, tags);
                student.getItems().add(created);
                student.setValue(created);
                name.clear();
                email.clear();
                interests.clear();
                message("Student registered successfully!");
            } catch (IllegalArgumentException ex) {
                message("Error: " + ex.getMessage());
            }
        });
        enroll.setOnAction(e -> {
            try {
                if (student.getValue() == null || course.getValue() == null) {
                    message("Select a student and a course.");
                    return;
                }
                platform.enroll(student.getValue().getId(), course.getValue().getId());
                message("Successfully enrolled in " + course.getValue().getTitle());
                refresh.run();
            } catch (IllegalArgumentException | IllegalStateException ex) {
                message("Error: " + ex.getMessage());
            }
        });
        student.setOnAction(e -> refresh.run());
        page("Register", "Register students and manage course enrollments",
                card(label("New student", 19, true), formRow("Full name", name),
                        formRow("Email", email), formRow("Interests", interests), register),
                card(label("Enroll in a course", 19, true), formRow("Student", student),
                        formRow("Course", course), enroll),
                label("My Courses", 20, true), myCourses);
        refresh.run();
    }

    private void showSubmitAssignment() {
        activate(5);
        ComboBox<Student> student = combo("Select student", platform.students());
        ComboBox<Course> course = combo("Select enrolled course", new ArrayList<>());
        ComboBox<Assignment> assignment = combo("Select assignment", new ArrayList<>());
        TextArea answer = new TextArea();
        answer.setPromptText("Write your answer here...");
        answer.setPrefRowCount(5);
        student.setOnAction(e -> {
            course.getItems().clear();
            assignment.getItems().clear();
            if (student.getValue() != null) {
                for (var enrollment : platform.enrollmentsOf(student.getValue().getId()))
                    course.getItems().add(enrollment.access().getCourse());
            }
        });
        course.setOnAction(e -> {
            assignment.getItems().clear();
            if (course.getValue() != null)
                assignment.getItems().addAll(platform.assignmentsOf(course.getValue().getId()));
        });
        Button submit = primary("Submit Answer");
        submit.setOnAction(e -> {
            try {
                if (student.getValue() == null || assignment.getValue() == null || answer.getText().isBlank()) {
                    message("Select a student, assignment and enter your answer.");
                    return;
                }
                Submission result = platform.submit(student.getValue().getId(),
                        assignment.getValue().getId(), answer.getText().trim());
                message("Submitted successfully!\nAssignment: " + result.getAssignment().getTitle());
                showDashboard();
            } catch (IllegalArgumentException | IllegalStateException ex) {
                message("Error: " + ex.getMessage());
            }
        });
        page("Submit Assignment", "Send coursework for instructor review",
                card(formRow("Student", student), formRow("Enrolled course", course),
                        formRow("Assignment", assignment), formRow("Your answer", answer), submit));
    }

    private void showGradeAssignments() {
        activate(6);
        VBox list = new VBox(15);
        var submissions = platform.pendingSubmissions();
        if (submissions.isEmpty()) list.getChildren().add(card(muted("No pending submissions. All caught up!")));
        for (Submission submission : submissions) {
            Label answer = muted("Answer: " + submission.getContent());
            TextField score = field("Score (0 - " + submission.getAssignment().getMaxScore() + ")");
            TextArea feedback = new TextArea();
            feedback.setPromptText("Write feedback...");
            feedback.setPrefRowCount(2);
            Button grade = primary("Submit Grade");
            grade.setOnAction(e -> {
                try {
                    int value = Integer.parseInt(score.getText().trim());
                    platform.grade(submission, value, feedback.getText().trim());
                    message("Graded successfully: " + value + "/" + submission.getAssignment().getMaxScore());
                    showGradeAssignments();
                } catch (NumberFormatException ex) {
                    message("Enter a valid integer score.");
                } catch (IllegalArgumentException | IllegalStateException ex) {
                    message("Error: " + ex.getMessage());
                }
            });
            list.getChildren().add(card(label(submission.getAssignment().getTitle(), 19, true),
                    muted("Student: " + submission.getStudent().getName()), answer,
                    muted("Maximum score: " + submission.getAssignment().getMaxScore()),
                    formRow("Score", score), formRow("Feedback", feedback), grade));
        }
        page("Grade Assignments", "Review student submissions and provide feedback", list);
    }

    private void showNotifications() {
        activate(9);
        VBox list = new VBox(12);
        Runnable refresh = () -> {
            list.getChildren().clear();
            var notifications = platform.notifications().all();
            if (notifications.isEmpty()) list.getChildren().add(card(muted("No notifications yet.")));
            for (Notification n : notifications) {
                list.getChildren().add(card(label(n.channel(), 15, true),
                        muted("To: " + n.recipient()), label(n.text(), 14, false)));
            }
        };
        Button reload = secondary("Refresh");
        reload.setOnAction(e -> refresh.run());
        Button clear = secondary("Clear All");
        clear.setOnAction(e -> {
            platform.notifications().clear();
            refresh.run();
        });
        page("Notifications", "Observer pattern: updates from the learning platform",
                new HBox(12, reload, clear), list);
        refresh.run();
    }

    private void showRecommendations() {
        activate(7);
        ComboBox<Student> student = combo("Select student", platform.students());
        ComboBox<String> strategy = combo("Choose recommendation strategy",
                List.of("Most Popular", "By Interests", "By Level", "By Budget"));
        strategy.setValue("Most Popular");
        TextField budget = field("Maximum budget ($)");
        budget.setText("50");
        budget.setDisable(true);
        strategy.setOnAction(e -> budget.setDisable(!"By Budget".equals(strategy.getValue())));
        Label active = muted("Current: " + platform.recommendationStrategy().name());
        VBox results = new VBox(12);
        Button recommend = primary("Find My Courses");
        recommend.setOnAction(e -> {
            try {
                if (student.getValue() == null) {
                    message("Please select a student.");
                    return;
                }
                RecommendationStrategy chosen;
                switch (strategy.getValue()) {
                    case "By Interests": chosen = new InterestBasedStrategy(); break;
                    case "By Level": chosen = new LevelBasedStrategy(); break;
                    case "By Budget":
                        double amount = Double.parseDouble(budget.getText().trim());
                        if (!Double.isFinite(amount) || amount < 0) {
                            message("Enter a valid budget.");
                            return;
                        }
                        chosen = new BudgetStrategy(amount);
                        break;
                    default: chosen = new PopularityStrategy();
                }
                platform.setRecommendationStrategy(chosen);
                active.setText("Current: " + platform.recommendationStrategy().name());
                results.getChildren().clear();
                List<Course> courses = platform.recommend(student.getValue().getId(), 10);
                if (courses.isEmpty()) results.getChildren().add(card(muted("No matching courses found.")));
                for (Course c : courses) {
                    results.getChildren().add(card(label(c.getTitle(), 18, true),
                            muted("Level: " + c.getLevel() + "   •   Instructor: " + c.getInstructor()),
                            muted("Tags: " + c.getTags() + "   •   Popularity: " + c.getPopularity()),
                            label(String.format("$%.2f", c.getBasePrice()), 17, true)));
                }
            } catch (NumberFormatException ex) {
                message("Please enter a valid budget.");
            } catch (IllegalArgumentException | IllegalStateException ex) {
                message("Error: " + ex.getMessage());
            }
        });
        page("Smart Recommendations", "Strategy pattern: switch algorithms instantly",
                card(formRow("Student", student), formRow("Recommendation strategy", strategy),
                        formRow("Budget", budget), active, recommend),
                label("Recommended for you", 20, true), results);
    }

    private void showCourseAddOns() {
        activate(8);
        ComboBox<Student> student = combo("Select student", platform.students());
        ComboBox<Course> course = combo("Select course", platform.courses());
        AddOn[] options = AddOn.values();
        List<CheckBox> checks = new ArrayList<>();
        VBox features = new VBox(12);
        for (AddOn option : options) {
            CheckBox check = new CheckBox(option.getLabel()
                    + String.format("   +$%.2f", option.getPrice()));
            check.setStyle("-fx-font-size: 14px; -fx-text-fill: " + TEXT + ";");
            checks.add(check);
            features.getChildren().add(check);
        }
        Label total = label("Total: $0.00", 24, true);
        Label details = muted("Choose a course to see its price.");
        Runnable update = () -> {
            if (course.getValue() == null) {
                total.setText("Total: $0.00");
                details.setText("Choose a course to see its price.");
                return;
            }
            List<AddOn> selected = new ArrayList<>();
            for (int i = 0; i < options.length; i++) if (checks.get(i).isSelected()) selected.add(options[i]);
            CourseAccess quote = platform.quote(course.getValue().getId(), selected.toArray(new AddOn[0]));
            total.setText(String.format("Total: $%.2f", quote.getPrice()));
            details.setText(quote.getDescription() + "\nFeatures: " + String.join(", ", quote.getFeatures()));
        };
        course.setOnAction(e -> update.run());
        for (CheckBox check : checks) check.setOnAction(e -> update.run());
        Button enroll = primary("Enroll with Add-ons");
        enroll.setOnAction(e -> {
            try {
                if (student.getValue() == null || course.getValue() == null) {
                    message("Select a student and course.");
                    return;
                }
                List<AddOn> selected = new ArrayList<>();
                for (int i = 0; i < options.length; i++) if (checks.get(i).isSelected()) selected.add(options[i]);
                AddOn[] chosen = selected.toArray(new AddOn[0]);
                CourseAccess quote = platform.quote(course.getValue().getId(), chosen);
                platform.enroll(student.getValue().getId(), course.getValue().getId(), chosen);
                message("Enrollment successful!\nCourse: " + course.getValue().getTitle()
                        + String.format("\nTotal paid: $%.2f", quote.getPrice()));
                showMyLearning();
            } catch (IllegalArgumentException | IllegalStateException ex) {
                message("Error: " + ex.getMessage());
            }
        });
        page("Course Add-ons", "Decorator pattern: customize your learning experience",
                card(formRow("Student", student), formRow("Course", course)),
                card(label("Customize your course", 19, true), features),
                card(total, details, enroll));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
