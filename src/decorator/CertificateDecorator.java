package decorator;
public class CertificateDecorator extends CourseAccessDecorator {
    public static final double PRICE = 19.99;
    public CertificateDecorator(CourseAccess wrapped) {
        super(wrapped);
    }
    @Override protected String featureName() {
        return "Verified certificate";
    }
    @Override protected double extraPrice() {
        return PRICE;
    }
}
