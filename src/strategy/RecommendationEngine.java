package strategy;
import builder.Course;
import model.Student;
import java.util.List;
import java.util.Objects;
import java.util.Set;
public class RecommendationEngine {
    private RecommendationStrategy strategy;
    public RecommendationEngine(RecommendationStrategy strategy) {
        this.strategy = Objects.requireNonNull(strategy);
    }
    public void setStrategy(RecommendationStrategy strategy) {
        this.strategy = Objects.requireNonNull(strategy);
    }
    public RecommendationStrategy getStrategy() {
        return strategy;
    }
    public List<Course> recommend(Student student, List<Course> catalog, Set<String> alreadyTaken, int limit) {
        List<Course> candidates = catalog.stream()
                .filter(c -> !alreadyTaken.contains(c.getId()))
                .filter(c -> !student.getCompletedCourseIds().contains(c.getId()))
                .toList();
        return strategy.recommend(student, candidates, limit);
    }
}
