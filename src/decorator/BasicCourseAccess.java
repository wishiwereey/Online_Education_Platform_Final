package decorator;
import builder.Course;
import java.util.List;
public class BasicCourseAccess implements CourseAccess {
    private final Course course;
    public BasicCourseAccess(Course course) {
        this.course = course;
    }
    @Override public Course getCourse() {
        return course;
    }
    @Override public String getDescription() {
        return course.getTitle();
    }
    @Override public double getPrice() {
        return course.getBasePrice();
    }
    @Override public List<String> getFeatures() {
        return List.of("Video lessons", "Quizzes");
    }
}
