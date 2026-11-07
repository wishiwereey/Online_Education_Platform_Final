package decorator;
public class SubtitlesDecorator extends CourseAccessDecorator {
    public static final double PRICE = 4.99;
    private final String language;
    public SubtitlesDecorator(CourseAccess wrapped, String language) {
        super(wrapped);
        this.language = language;
    }
    @Override protected String featureName() {
        return "Subtitles (" + language + ")";
    }
    @Override protected double extraPrice() {
        return PRICE;
    }
}
