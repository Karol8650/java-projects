import java.util.HashMap;
import java.util.Map;

public class ScoreToAge {
    private static final int minAge = 5;
    private static final int minScore = 1;
    private static final int maxScore = 13;
    private static final String maxAgeRange = "18-22";


    private static final Map<Integer, String> scoreToAge;

    static {
        scoreToAge = new HashMap<>();
        initializeMap();
    }

    private static void initializeMap() {
        int age = minAge;
        for (int score = minScore; score <= maxScore; score++, age++) {
            String ageRange = age + "-" + (age + 1);
            scoreToAge.put(score, ageRange);
        }
        scoreToAge.put(maxScore + 1, maxAgeRange);
    }

    static int getUpperBoundAge(int score) {
        String ageRange = scoreToAge.get(score);
        return Integer.parseInt(ageRange.substring(ageRange.lastIndexOf('-') + 1));
    }
}
