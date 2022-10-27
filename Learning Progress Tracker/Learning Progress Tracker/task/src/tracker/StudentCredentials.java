package tracker;

class StudentCredentials {
    private final String firstName;
    private final String lastName;
    private final String email;

    StudentCredentials(String credentials) {
        int firstSpaceIndex = credentials.indexOf(' ');
        int lastSpaceIndex = credentials.lastIndexOf(' ');

        firstName = credentials.substring(0, firstSpaceIndex);
        lastName = credentials.substring(firstSpaceIndex + 1, lastSpaceIndex);
        email = credentials.substring(lastSpaceIndex + 1);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }
}
