import builder.Course;
import decorator.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DecoratorTest {
    private final Course course = Course.builder("D-1", "Course").price(50).addModule("M", "L").build();

    @Test
    void basicAccessCostsBasePrice() {
        CourseAccess a = new BasicCourseAccess(course);
        assertEquals(50.0, a.getPrice(), 1e-9);
        assertEquals(2, a.getFeatures().size());
    }

    @Test
    void decoratorsStackPricesAndFeatures() {
        CourseAccess a = new MentorSupportDecorator(new CertificateDecorator(new BasicCourseAccess(course)));
        assertEquals(50 + CertificateDecorator.PRICE + MentorSupportDecorator.PRICE, a.getPrice(), 1e-9);
        assertEquals("Course + Verified certificate + Mentor support", a.getDescription());
        assertTrue(a.getFeatures().containsAll(java.util.List.of("Verified certificate", "Mentor support")));
        assertSame(course, a.getCourse());
    }

    @Test
    void decoratorCanHaveOwnState() {
        CourseAccess a = new SubtitlesDecorator(new BasicCourseAccess(course), "Spanish");
        assertTrue(a.getDescription().endsWith("Subtitles (Spanish)"));
    }

    @Test
    void addOnEnumAppliesMatchingDecorator() {
        CourseAccess a = AddOn.OFFLINE.applyTo(new BasicCourseAccess(course));
        assertInstanceOf(OfflineAccessDecorator.class, a);
    }
}
