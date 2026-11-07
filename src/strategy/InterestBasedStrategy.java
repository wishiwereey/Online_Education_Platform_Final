package strategy;
import builder.Course;
import model.Student;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
public class InterestBasedStrategy implements RecommendationStrategy {
    @Override public String name() {
        return "By interests";
    }
    @Override
    public List<Course> recommend(Student student, List<Course> candidates, int limit) {
        Set<String> interests = student.getInterests();
        return candidates.stream()
                .filter(c -> matches(c, interests) > 0)
                .sorted(Comparator.comparingLong((Course c) -> matches(c, interests)).reversed()
                        .thenComparing(Comparator.comparingInt(Course::getPopularity).reversed()))
                .limit(limit)
                .toList();
    }
    static long matches(Course c, Set<String> interests) {
        return c.getTags().stream().filter(interests::contains).count();
    }
}
