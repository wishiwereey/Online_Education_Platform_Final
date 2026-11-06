package builder;

import model.Level;
import model.Module;
import model.Quiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
public final class Course {
    private final String id;
    private final String title;
    private final String instructor;
    private final Level level;
    private final double basePrice;
    private final int maxStudents;
    private final List<Module> modules;
    private final List<Quiz> quizzes;
    private final Set<String> tags;
    private int popularity; // number of enrollments, changes at runtime
    private Course(Builder b) {
        this.id = b.id;
        this.title = b.title;
        this.instructor = b.instructor;
        this.level = b.level;
        this.basePrice = b.basePrice;
        this.maxStudents = b.maxStudents;
        this.modules = List.copyOf(b.modules);
        this.quizzes = List.copyOf(b.quizzes);
        this.tags = Collections.unmodifiableSet(new LinkedHashSet<>(b.tags));
        this.popularity = b.initialPopularity;
    }
    public static Builder builder(String id, String title) {
        return new Builder(id, title);
    }
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getInstructor() { return instructor; }
    public Level getLevel() { return level; }
    public double getBasePrice() { return basePrice; }
    public int getMaxStudents() { return maxStudents; }
    public List<Module> getModules() { return modules; }
    public List<Quiz> getQuizzes() { return quizzes; }
    public Set<String> getTags() { return tags; }
    public int getPopularity() { return popularity; }
    public void incrementPopularity() { popularity++; }
    public int getLessonCount() {
        return modules.stream().mapToInt(m -> m.getLessons().size()).sum();
    }
    @Override
    public String toString() {
        return String.format("[%s] %s (%s, by %s, $%.2f)", id, title, level, instructor, basePrice);
    }
    public static final class Builder {
        private final String id;
        private final String title;
        private String instructor = "TBA";
        private Level level = Level.BEGINNER;
        private double basePrice = 0.0;
        private int maxStudents = 100;
        private int initialPopularity = 0;
        private final List<Module> modules = new ArrayList<>();
        private final List<Quiz> quizzes = new ArrayList<>();
        private final Set<String> tags = new LinkedHashSet<>();
        private Builder(String id, String title) {
            this.id = id;
            this.title = title;
        }
        public Builder instructor(String instructor) { this.instructor = instructor; return this; }
        public Builder level(Level level) { this.level = level; return this; }
        public Builder price(double price) { this.basePrice = price; return this; }
        public Builder maxStudents(int max) { this.maxStudents = max; return this; }
        public Builder popularity(int popularity) { this.initialPopularity = popularity; return this; }
        public Builder addModule(String title, String... lessons) {
            modules.add(new Module(title, List.of(lessons)));
            return this;
        }
        public Builder addQuiz(String title, int questions, int passingScore) {
            quizzes.add(new Quiz(title, questions, passingScore));
            return this;
        }
        public Builder tags(String... tags) {
            this.tags.addAll(List.of(tags));
            return this;
        }
        public Course build() {
            if (id == null || id.isBlank()) throw new IllegalStateException("Course id is required");
            if (title == null || title.isBlank()) throw new IllegalStateException("Course title is required");
            if (modules.isEmpty()) throw new IllegalStateException("Course '" + title + "' must have at least one module");
            if (basePrice < 0) throw new IllegalStateException("Price cannot be negative");
            if (maxStudents <= 0) throw new IllegalStateException("maxStudents must be positive");
            return new Course(this);
        }
    }
}
