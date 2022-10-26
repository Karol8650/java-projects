import java.util.Arrays;
import java.util.regex.Pattern;

class TextProcessor {
    private final String text;
    private TextStats textStats;
    private int sumOfAges;
    private static final int polysyllablesMin = 3;

    TextProcessor(String text) {
        this.text = text;
        sumOfAges = 0;
        calculateStats(text);
    }

    private void calculateStats(String fileContent) {
        textStats = new TextStats();
        textStats.setSentences(countSentences(fileContent));
        textStats.setWords(countWords(fileContent));
        textStats.setCharacters(countCharacters(fileContent));
        textStats.setSyllables(countSyllables(fileContent));
        textStats.setPolysyllables(countPolysyllables(fileContent));
    }

    private static int countSyllables(String fileContent) {
        return (int) Pattern.compile(Regex.syllables).matcher(fileContent).results().count(); // matcher(CharSequence) is method from Pattern (java 8),
        // results() is from Matcher (java 9) and return stream, count() returns long
    }

    private static int countPolysyllables(String fileContent) {

        return (int) Arrays.stream(fileContent.split("\\s+"))
                .filter(word -> Pattern.compile(Regex.syllables).matcher(word).results().count() >= polysyllablesMin)
                .count();

    }

    private static int countSentences(String fileContent) {
        return fileContent.split(Regex.sentences).length;
    }

    private static int countWords(String fileContent) {
        return fileContent.split(Regex.words).length;
    }

    private static int countCharacters(String fileContent) {
        return fileContent.replaceAll(Regex.characters, "").length();
    }

    public void displayContent() {
        System.out.println("The text is:");
        System.out.println(text);
    }

    public void displayStats() {
        System.out.printf("\nWords: %d\n", textStats.getWords());
        System.out.printf("Sentences: %d\n", textStats.getSentences());
        System.out.printf("Characters: %d\n", textStats.getCharacters());
        System.out.printf("Syllables: %d\n", textStats.getSyllables());
        System.out.printf("Polysyllables: %d\n", textStats.getPolysyllables());
    }


    public void getSummary(String nameOfScore) {
        if (nameOfScore.equals("all")) {
            displaySummary(extractSummary("ARI"));
            displaySummary(extractSummary("FK"));
            displaySummary(extractSummary("SMOG"));
            displaySummary(extractSummary("CL"));
            double averageAge = calculateAverageAge();
            System.out.printf("This text should be understood in average by %.2f-year-olds.", averageAge);

        } else {
            String summary = extractSummary(nameOfScore);
            displaySummary(summary);
        }
    }

    private double calculateAverageAge() {
        final int totalNumOfIndexes = 4;
        return (double) sumOfAges / totalNumOfIndexes;
    }


    private String extractSummary(String nameOfScore) {
        Score index = Score.getIndex(nameOfScore, textStats);
        double score = index.getScore();
        String fullName = index.getFullName();
        int age = ScoreToAge.getUpperBoundAge((int) Math.ceil(score));
        sumOfAges += age;
        return fullName + ": " + score + " (about " + age + "-year-olds).";
    }

    private void displaySummary(String summary) {
        System.out.println(summary);
    }

}
