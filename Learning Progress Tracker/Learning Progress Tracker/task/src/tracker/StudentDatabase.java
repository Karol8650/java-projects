package tracker;

import java.util.ArrayList;
import java.util.List;

class StudentDatabase {
    private static final List<Student> students = new ArrayList<>();

    static void addStudent(Student student) {
        students.add(student);
    }

    static int numOfStudents() {
        return students.size();
    }
}
