package model;

/** Difficulty level of a course and the current level of a student. */
public enum Level {
    BEGINNER, INTERMEDIATE, ADVANCED;

    /** The level a student should aim for after this one (ADVANCED stays ADVANCED). */
    public Level next() {
        return this == ADVANCED ? ADVANCED : values()[ordinal() + 1];
    }
}
