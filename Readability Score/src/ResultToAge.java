import java.util.HashMap;
import java.util.Map;

public class ResultToAge {
    private static Map<Integer, String> resultToAge;
    public ResultToAge() {
        resultToAge = new HashMap<>();
        initializeMap();
    }

    private static void initializeMap() {
        int age = 5;
        for (int score = 1; score <= 13; score++, age++) {
            String ageRange = age + "-" + (age + 1);
            resultToAge.put(score, ageRange);
        }
        resultToAge.put(14, "18-22");
    }

    int getUpperBoundAge(int score) {
        String ageRange = resultToAge.get(score);
        return Integer.parseInt(ageRange.substring(ageRange.lastIndexOf('-') + 1));
    }
}
