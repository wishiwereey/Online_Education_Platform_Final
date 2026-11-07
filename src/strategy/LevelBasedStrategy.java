package strategy;
import builder.Course;
import model.Level;
import model.Student;
import java.util.Comparator;
import java.util.List;
public class LevelBasedStrategy implements RecommendationStrategy {
    @Override public String name() {
        return "Next step by level";
    }
    @Override
    public List<Course> recommend(Student student, List<Course> candidates, int limit) {
        Level current = student.getLevel();
        Level next = current.next();
        return candidates.stream()
                .filter(c -> c.getLevel() == current || c.getLevel() == next)
                .sorted(Comparator.comparingInt((Course c) -> c.getLevel() == current ? 0 : 1)
                        .thenComparing(Comparator.comparingInt(Course::getPopularity).reversed()))
                .limit(limit)
                .toList();
    }
}
