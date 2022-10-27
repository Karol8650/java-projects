package tracker;

import java.util.Arrays;
import java.util.regex.Pattern;

public class DataVerification {
    static private final String nameRegex = "([a-zA-Z][-']?)+[a-zA-Z]$";

    static private final String emailRegex = "((\".*\")" +
            "|(\\w+([\\.\\+\\!\\%/-]\\w+)*-?))" +
            "@\\w+([\\.\\+/-]\\w+)*\\.\\w+";

    static private final int minCredentialParts = 3;

    static private boolean verifyName(String name) {
        boolean result = Pattern.compile(nameRegex).matcher(name).matches();

        if (!result) {
            System.out.println("Incorrect first name.");
        }

        return result;
    }

    static private boolean verifyLastName(String lastName) {
        String[] lastNameParts = lastName.split("\\s+");

        int properParts = (int) Arrays.stream(lastNameParts)
                .filter(word -> Pattern.compile(nameRegex).matcher(word).matches()).count();

        boolean result = (properParts == lastNameParts.length);

        if (!result) {
            System.out.println("Incorrect last name.");
        }

        return result;
    }

    static private boolean verifyEmail(String email) {
        boolean result = Pattern.compile(emailRegex).matcher(email).matches();

        if (!result) {
            System.out.println("Incorrect email.");
        }

        return result;
    }

    static boolean checkCredentials(String credentials) {
        if (credentials.trim().split("\s+").length < minCredentialParts) {
            System.out.println("Incorrect credentials.");
            return false;
        }

        credentials = credentials.replaceAll("\s{2,}", " ");

        StudentCredentials studentCredentials = new StudentCredentials(credentials);

        return verifyName(studentCredentials.getFirstName()) &&
                verifyLastName(studentCredentials.getLastName()) &&
                verifyEmail(studentCredentials.getEmail());
    }
}
