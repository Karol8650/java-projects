import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

class TextStats {
    private int Sentences;
    private int Words;
    private int Characters;

    public int getSentences() {
        return Sentences;
    }

    public void setSentences(int Sentences) {
        this.Sentences = Sentences;
    }

    public int getWords() {
        return Words;
    }

    public void setWords(int Words) {
        this.Words = Words;
    }

    public int getCharacters() {
        return Characters;
    }

    public void setCharacters(int Characters) {
        this.Characters = Characters;
    }

}

public class Main {
    private static String loadFileContent(String path) throws IOException {
        byte[] byteFileContent = Files.readAllBytes(Paths.get(path)); // read file as byte array
        return new String(byteFileContent);
    }

    private static void displayContent(String content) {
        System.out.println("The text is:");
        System.out.println(content);
    }

    private static void initializeMap(Map<Integer, String> resultToAge) {
        int age = 5;
        for (int score = 1; score <= 13; score++, age++) {
            String ageRange = age + "-" + (age + 1);
            resultToAge.put(score, ageRange);
        }
        resultToAge.put(14, "18-22");
    }

    private static TextStats processText(String fileContent) {
        String[] sentences = fileContent.split("[.?!]\\s*");
        int numOfWords = 0;
        int numOfCharacters = 0;
        int numOfSentences = sentences.length;
        for (String sentence : sentences) {
            String[] words = sentence.split("\\s");
            numOfWords += words.length;
            for (String word : words) {
                numOfCharacters += word.length();
            }
        }

        char c = fileContent.charAt(fileContent.length() - 1);
        if (c == '.' || c == '?' || c == '!') {
            numOfCharacters += sentences.length; // add punctuation marks, this is case when there is one at the end of the sentence
        }
        else {
            numOfCharacters += sentences.length - 1; // when there is not...
        }

        TextStats stats = new TextStats();
        stats.setSentences(numOfSentences);
        stats.setWords(numOfWords);
        stats.setCharacters(numOfCharacters);
        return stats;
    }

    private static double calculateScore(TextStats stats) {
        return 4.71 * ((double)stats.getCharacters() / (double)stats.getWords()) // formula for Automated readability index
                + 0.5 * ((double)stats.getWords() / (double)stats.getSentences()) - 21.43;
    }

    private static String calculateAge(int score, Map<Integer, String> resultToAge) {
        return resultToAge.get(score);
    }

    private static void displaySummary(TextStats stats, double score, String ageRange) {
        System.out.printf("\nWords: %d\n", stats.getWords());
        System.out.printf("Sentences: %d\n", stats.getSentences());
        System.out.printf("Characters: %d\n" ,stats.getCharacters());
        System.out.printf("The score is: %.2f\n", score);
        System.out.printf("This text should be understood by %s year-olds.", ageRange);
    }


    public static void main(String[] args) {
        Map<Integer, String> resultToAge = new HashMap<>(); // ratio of result to age
        initializeMap(resultToAge);

        try {
            String fileContent = loadFileContent(args[0]); // file content in single String
            displayContent(fileContent);
            TextStats stats = processText(fileContent);
            double score = calculateScore(stats);
            String ageRange = calculateAge((int) Math.ceil(score), resultToAge);
            displaySummary(stats, score, ageRange);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}