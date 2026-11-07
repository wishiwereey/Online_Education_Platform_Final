package decorator;
public class MentorSupportDecorator extends CourseAccessDecorator {
    public static final double PRICE = 49.00;
    public MentorSupportDecorator(CourseAccess wrapped) {
        super(wrapped);
    }
    @Override protected String featureName() {
        return "Mentor support";
    }
    @Override protected double extraPrice() {
        return PRICE;
    }
}
