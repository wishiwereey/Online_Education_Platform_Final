package strategy;
import builder.Course;
import model.Student;
import java.util.List;
public interface RecommendationStrategy {
    String name();
    List<Course> recommend(Student student, List<Course> candidates, int limit);
}
