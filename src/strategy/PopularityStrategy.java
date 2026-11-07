package strategy;
import builder.Course;
import model.Student;
import java.util.Comparator;
import java.util.List;
public class PopularityStrategy implements RecommendationStrategy {
    @Override public String name() {
        return "Most popular";
    }
    @Override
    public List<Course> recommend(Student student, List<Course> candidates, int limit) {
        return candidates.stream()
                .sorted(Comparator.comparingInt(Course::getPopularity).reversed()
                        .thenComparing(Course::getTitle))
                .limit(limit)
                .toList();
    }
}
