package decorator;
import builder.Course;
import java.util.List;
public interface CourseAccess {
    Course getCourse();
    String getDescription();
    double getPrice();
    List<String> getFeatures();
}
