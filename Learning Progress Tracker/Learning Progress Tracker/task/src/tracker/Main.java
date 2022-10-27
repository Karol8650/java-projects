package tracker;

import java.util.Objects;
import java.util.Scanner;

public class Main {
    private final static String TITLE = "Learning Progress Tracker";
    private final static Scanner scanner = new Scanner(System.in);

    static private void getInput() {
        String input = scanner.nextLine().trim();

        while (!Objects.equals(input, "exit")) {

            switch (input) {
                case "" -> System.out.println("no input");
                case "back" -> System.out.println("Enter 'exit' to exit the program.");
                case "add students" -> {
                    System.out.println("Enter student credentials or 'back' to return:");
                    input = scanner.nextLine().trim();
                    while (!Objects.equals(input, "back")) {

                        if (DataVerification.checkCredentials(input)) {
                            Student student = new Student(input);
                            StudentDatabase.addStudent(student);
                            System.out.println("The student has been added.");
                        }

                        input = scanner.nextLine().trim();
                    }
                    int totalStudents = StudentDatabase.numOfStudents();
                    System.out.println("Total " + totalStudents + " students have been added.");
                }
                default -> System.out.println("Error: unknown command!");
            }

            input = scanner.nextLine().trim();

        }
        System.out.println("Bye!");
    }

    public static void main(String[] args) {
        System.out.println(TITLE);
        getInput();
    }
}
