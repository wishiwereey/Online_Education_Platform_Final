package decorator;
public class OfflineAccessDecorator extends CourseAccessDecorator {
    public static final double PRICE = 9.99;
    public OfflineAccessDecorator(CourseAccess wrapped) {
        super(wrapped);
    }
    @Override protected String featureName() {
        return "Offline access";
    }
    @Override protected double extraPrice() {
        return PRICE;
    }
}
