import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class Main {

    private static String loadFileContentToString(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }

    public static void main(String[] args) {

        try {
            String fileContent = loadFileContentToString(args[0]);

            TextProcessor textProcessor = new TextProcessor(fileContent);

            textProcessor.displayContent();

            textProcessor.displayStats();

            System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
            Scanner scanner = new Scanner(System.in);
            String nameOfScore = scanner.next();
            textProcessor.getSummary(nameOfScore);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}