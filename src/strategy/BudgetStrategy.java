package strategy;
import builder.Course;
import model.Student;
import java.util.Comparator;
import java.util.List;
public class BudgetStrategy implements RecommendationStrategy {
    private final double maxPrice;
    public BudgetStrategy(double maxPrice) {
        this.maxPrice = maxPrice;
    }
    @Override public String name() {
        return String.format("Budget (<= $%.0f)", maxPrice);
    }
    @Override
    public List<Course> recommend(Student student, List<Course> candidates, int limit) {
        return candidates.stream()
                .filter(c -> c.getBasePrice() <= maxPrice)
                .sorted(Comparator.comparingDouble(Course::getBasePrice)
                        .thenComparing(Comparator.comparingInt(Course::getPopularity).reversed()))
                .limit(limit)
                .toList();
    }
}
