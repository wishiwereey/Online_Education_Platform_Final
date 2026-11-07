package builder;
import model.Level;
public class CourseDirector {
    public Course buildIntroCourse(String id, String topic, String instructor) {
        return Course.builder(id, "Introduction to " + topic)
                .instructor(instructor)
                .level(Level.BEGINNER)
                .price(29.99)
                .addModule("Getting started", "What is " + topic, "Setting up tools")
                .addModule("First steps", "Core ideas", "Hands-on example")
                .addQuiz("Intro check", 5, 60)
                .tags(slug(topic), "intro")
                .build();
    }
    public Course buildBootcamp(String id, String topic, String instructor, double price) {
        return Course.builder(id, topic + " Bootcamp")
                .instructor(instructor)
                .level(Level.INTERMEDIATE)
                .price(price)
                .maxStudents(30)
                .addModule("Week 1: Foundations", "Syntax", "Tooling", "Testing basics")
                .addModule("Week 2: Practice", "Patterns", "Refactoring", "Code review")
                .addModule("Week 3: Project", "Planning", "Implementation", "Demo day")
                .addQuiz("Midterm", 20, 70)
                .addQuiz("Final exam", 40, 75)
                .tags(slug(topic), "bootcamp", "project")
                .build();
    }
    private static String slug(String topic) {
        return topic.toLowerCase().replace(' ', '-');
    }
}
