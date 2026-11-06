package service;
import decorator.CourseAccess;
import model.Student;
public record Enrollment(Student student, CourseAccess access, double amountPaid) {
    public String courseId() { return access.getCourse().getId(); }
}
