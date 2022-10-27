package tracker;

class Student {
    private final StudentCredentials studentCredentials;
    Student(String credentials) {
        studentCredentials = new StudentCredentials(credentials);
    }
}
