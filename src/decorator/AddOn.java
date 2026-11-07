package decorator;
import java.util.function.UnaryOperator;
public enum AddOn {
    CERTIFICATE("Verified certificate", CertificateDecorator.PRICE, CertificateDecorator::new),
    MENTOR("Mentor support", MentorSupportDecorator.PRICE, MentorSupportDecorator::new),
    OFFLINE("Offline access", OfflineAccessDecorator.PRICE, OfflineAccessDecorator::new),
    SUBTITLES_RU("Subtitles (Russian)", SubtitlesDecorator.PRICE, a -> new SubtitlesDecorator(a, "Russian")),
    SUBTITLES_EN("Subtitles (English)", SubtitlesDecorator.PRICE, a -> new SubtitlesDecorator(a, "English"));
    private final String label;
    private final double price;
    private final UnaryOperator<CourseAccess> decorate;
    AddOn(String label, double price, UnaryOperator<CourseAccess> decorate) {
        this.label = label;
        this.price = price;
        this.decorate = decorate;
    }
    public String getLabel() {
        return label;
    }
    public double getPrice() {
        return price;
    }
    public CourseAccess applyTo(CourseAccess access) {
        return decorate.apply(access);
    }
}
