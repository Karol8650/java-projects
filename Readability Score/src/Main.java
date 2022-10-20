import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TextStats {
    private int sentences;
    private int words;
    private int characters;
    private int syllables;
    private int polysyllables;

    public int getSentences() {
        return sentences;
    }

    public void setSentences(int sentences) {
        this.sentences = sentences;
    }

    public int getWords() {
        return words;
    }

    public void setWords(int words) {
        this.words = words;
    }

    public int getCharacters() {
        return characters;
    }

    public void setCharacters(int characters) {
        this.characters = characters;
    }
    public int getSyllables() {
        return syllables;
    }

    public void setSyllables(int syllables) {
        this.syllables = syllables;
    }

    public int getPolysyllables() {
        return polysyllables;
    }

    public void setPolysyllables(int polysyllables) {
        this.polysyllables = polysyllables;
    }


}

public class Main {
    private static ResultToAge resultToAge = new ResultToAge();
    private static TextStats stats;

    private static void displayContent(String content) {
        System.out.println("The text is:");
        System.out.println(content);
    }


    private static int countSyllables(String fileContent) {
        Pattern syllablesPattern = Pattern.compile(
                "\\b[^aeyuioAEYUIO\\s]+[aeyuioAEYUIO]\\b" + // matches whole word starting with one or more consonants and ending with one vowel (e.g. "the")
                "|[aeyuioAEYUIO]*[eE](?!\\b)[aeyuioAEYUIO]*" + // matches series of vowels containing "eE", but not ending with "eE" or punctuation mark (e.g. "ea", "e")
                "|[ayuioAYUIO]+" + // matches one or more vowels except for "eE"
                "|\\d+[,.]\\d*"); // matches numbers like 142,262
        Matcher syllablesMatcher = syllablesPattern.matcher(fileContent);
        int syllables = 0;
        while (syllablesMatcher.find()) {
            syllables++;
        }
        return syllables;
    }

    private static int countPolysyllables(String fileContent) {
        String[] words = fileContent.split(" ");
        int polysyllables = 0;
        for (String word : words) {
            if (countSyllables(word) > 2) {
                polysyllables++;
            }
        }
        return polysyllables;
    }

    private static int countSentences(String fileContent) {
        return fileContent.split("[.?!]\\s*").length;
    }

    private static int countWords(String fileContent) {
        return fileContent.split("\\s+").length;
    }

    private static int countCharacters(String fileContent) {
        return fileContent.replaceAll("\\s+", "").length();
    }

    private static TextStats processText(String fileContent) {
        int numOfSentences = countSentences(fileContent);
        int numOfWords = countWords(fileContent);
        int numOfCharacters = countCharacters(fileContent);
        int syllables = countSyllables(fileContent);
        int polysyllables = countPolysyllables(fileContent);


        TextStats stats = new TextStats();
        stats.setSentences(numOfSentences);
        stats.setWords(numOfWords);
        stats.setCharacters(numOfCharacters);
        stats.setSyllables(syllables);
        stats.setPolysyllables(polysyllables);
        return stats;
    }

    private static double calculateAutomatedReadabilityIndex(TextStats stats) {
        return 4.71 * ((double)stats.getCharacters() / (double)stats.getWords()) // formula for Automated readability index
                + 0.5 * ((double)stats.getWords() / (double)stats.getSentences()) - 21.43;
    }

    private static double calculateFleschKincaidReadabilityTests(TextStats stats) {
        return 0.39 * ((double)stats.getWords() / (double)stats.getSentences())
                + 11.8 * ((double)stats.getSyllables() / (double)stats.getWords()) - 15.59;
    }

    private static double calculateSmogIndex(TextStats stats) {
        return 1.043 * Math.sqrt(stats.getPolysyllables() * (30.0 / stats.getSentences())) + 3.1291;
    }

    private static double calculateColemanLiauIndex(TextStats stats) {
        final double L = averageNumOfCharacters(stats);
        final double S = averageNumOfSentences(stats);
        return 0.0588 * L - 0.296 * S - 15.8;
    }

    private static double averageNumOfCharacters(TextStats stats) {
        int characters = stats.getCharacters();
        int words = stats.getWords();
        return ((double)characters / (double)words) * 100;
    }

    private static double averageNumOfSentences(TextStats stats) {
        int sentences = stats.getSentences();
        int words = stats.getWords();
        return ((double)sentences / (double)words) * 100;
    }




    private static void displaySummary(TextStats stats) {
        System.out.printf("\nWords: %d\n", stats.getWords());
        System.out.printf("Sentences: %d\n", stats.getSentences());
        System.out.printf("Characters: %d\n" ,stats.getCharacters());
        System.out.printf("Syllables: %d\n" ,stats.getSyllables());
        System.out.printf("Polysyllables: %d\n" ,stats.getPolysyllables());
    }

    private static int handleARI() {
        double score = calculateAutomatedReadabilityIndex(stats);
        int ageRange = resultToAge.getUpperBoundAge((int) Math.ceil(score));
        System.out.printf("Automated Readability Index: %.2f (about %d-year-olds).\n", score, ageRange);
        return ageRange;
    }

    private static int handleFK() {
        double score = calculateFleschKincaidReadabilityTests(stats);
        int ageRange = resultToAge.getUpperBoundAge((int) Math.ceil(score));
        System.out.printf("Flesch–Kincaid readability tests: %.2f (about %d-year-olds).\n", score, ageRange);
        return ageRange;
    }

    private static int handleSMOG() {
        double score = calculateSmogIndex(stats);
        int ageRange = resultToAge.getUpperBoundAge((int) Math.ceil(score));
        System.out.printf("Simple Measure of Gobbledygook: %.2f (about %d-year-olds).\n", score, ageRange);
        return ageRange;
    }

    private static int handleCL() {
        double score = calculateColemanLiauIndex(stats);
        int ageRange = resultToAge.getUpperBoundAge((int) score);
        System.out.printf("Coleman–Liau index: %.2f (about %d-year-olds).\n", score, ageRange);
        return ageRange;
    }

    private static void handleALL() {
        int[] age = new int[4];
        age[0] = handleARI();
        age[1] = handleFK();
        age[2] = handleSMOG();
        age[3] = handleCL();
        double averageAge = calculateAverageAge(age);
        System.out.printf("This text should be understood in average by %.2f-year-olds.", averageAge);
    }

    private static double calculateAverageAge(int[] age) {
        int ageSum = 0;
        for (int x: age) {
            ageSum += x;
        }
        return (double)ageSum / 4;
    }

    private static String loadFileContentToString(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }
    public static void main(String[] args) {
        try {
            String fileContent = loadFileContentToString(args[0]);
            displayContent(fileContent);
            stats = processText(fileContent);
            displaySummary(stats);
            System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
            Scanner scanner = new Scanner(System.in);
            String algorithm = scanner.next();
            switch (algorithm) {
                case "ARI" -> handleARI();
                case "FK" -> handleFK();
                case "SMOG" -> handleSMOG();
                case "CL" -> handleCL();
                case "all" -> handleALL();
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}