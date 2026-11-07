import builder.Course;
import builder.CourseDirector;
import facade.LearningPlatformFacade;
import model.Level;
public final class DemoData {
    private DemoData() { }
    public static void seedCourses(LearningPlatformFacade platform) {
        CourseDirector director = new CourseDirector();
        platform.publishCourse(Course.builder("JAVA-101", "Java Programming Basics")
                .instructor("A. Smagulova")
                .level(Level.BEGINNER)
                .price(39.99)
                .popularity(120)
                .addModule("Syntax", "Variables", "Conditions", "Loops")
                .addModule("OOP", "Classes", "Inheritance", "Interfaces")
                .addQuiz("OOP quiz", 10, 60)
                .tags("java", "programming", "oop")
                .build());
        platform.publishCourse(Course.builder("DP-201", "Design Patterns in Java")
                .instructor("B. Akhmetov")
                .level(Level.INTERMEDIATE)
                .price(59.00)
                .popularity(85)
                .addModule("Creational", "Builder", "Factory Method", "Singleton")
                .addModule("Structural", "Decorator", "Facade", "Adapter")
                .addModule("Behavioral", "Observer", "Strategy", "Command")
                .addQuiz("Creational quiz", 8, 70)
                .addQuiz("Final test", 20, 70)
                .tags("java", "design-patterns", "oop", "architecture")
                .build());
        platform.publishCourse(Course.builder("ML-301", "Machine Learning Fundamentals")
                .instructor("D. Nurlanova")
                .level(Level.ADVANCED)
                .price(89.00)
                .popularity(150)
                .addModule("Math refresher", "Linear algebra", "Probability")
                .addModule("Models", "Regression", "Trees", "Neural networks")
                .addQuiz("Models quiz", 15, 70)
                .tags("python", "data-science", "ai")
                .build());
        platform.publishCourse(Course.builder("UX-110", "UX Design Essentials")
                .instructor("M. Lee")
                .level(Level.BEGINNER)
                .price(25.00)
                .popularity(60)
                .addModule("Research", "Interviews", "Personas")
                .addModule("Prototyping", "Wireframes", "Usability tests")
                .tags("design", "ux")
                .build());
        platform.publishCourse(Course.builder("CC-220", "Clean Code and Refactoring")
                .instructor("A. Smagulova")
                .level(Level.INTERMEDIATE)
                .price(45.00)
                .popularity(70)
                .addModule("Readable code", "Naming", "Functions", "Comments")
                .addModule("Refactoring", "Code smells", "Safe refactoring steps")
                .addQuiz("Code smells quiz", 10, 65)
                .tags("java", "oop", "architecture")
                .build());
        platform.publishCourse(director.buildIntroCourse("PY-100", "Python", "R. Ospanov"));
        platform.publishCourse(director.buildBootcamp("SPRING-250", "Spring Boot", "B. Akhmetov", 120.00));
    }
    public static void seedStudents(LearningPlatformFacade platform) {
        platform.registerStudent("S1", "Aruzhan", "aruzhan@student.edu", Level.BEGINNER, "java", "oop", "design-patterns");
        platform.registerStudent("S2", "Daniyar", "daniyar@student.edu", Level.INTERMEDIATE, "python", "ai", "data-science");
        platform.registerStudent("S3", "Madina", "madina@student.edu", Level.BEGINNER, "design", "ux");
    }
}
