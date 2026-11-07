package decorator;
import builder.Course;
import java.util.ArrayList;
import java.util.List;
public abstract class CourseAccessDecorator implements CourseAccess {
    protected final CourseAccess wrapped;
    protected CourseAccessDecorator(CourseAccess wrapped) {
        this.wrapped = wrapped;
    }
    protected abstract String featureName();
    protected abstract double extraPrice();
    @Override public Course getCourse() {
        return wrapped.getCourse();
    }
    @Override public String getDescription() {
        return wrapped.getDescription() + " + " + featureName();
    }
    @Override public double getPrice() {
        return wrapped.getPrice() + extraPrice();
    }
    @Override
    public List<String> getFeatures() {
        List<String> features = new ArrayList<>(wrapped.getFeatures());
        features.add(featureName());
        return features;
    }
}
