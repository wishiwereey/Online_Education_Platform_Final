package factory;

import java.time.LocalDate;

/** Product interface of the FACTORY METHOD pattern: any kind of assignment. */
public interface Assignment {
    String getId();
    String getTitle();
    String getCourseId();
    LocalDate getDeadline();
    int getMaxScore();
    String getType();
    /** Instructions shown to the student; each type explains itself differently. */
    String getInstructions();
}
