public abstract class Score {
    protected static TextStats textStats;
    protected final int precision = 2;

    Score(TextStats textStats) {
        Score.textStats = textStats;
    }

    public abstract double getScore();

    public abstract String getFullName();

    protected double truncate(double n) {
        int whole = (int) (n * Math.pow(10, precision));
        return whole / Math.pow(10, precision);
    }

    static Score getIndex(String score, TextStats textStats) {
        Score index = null;
        switch (score) {
            case "ARI"  ->  index = new ARI(textStats);
            case "FK"   ->  index = new FK(textStats);
            case "SMOG" ->  index = new SMOG(textStats);
            case "CL"   ->  index = new CL(textStats);
        }
        return index;
    }
}

class ARI extends Score {
    public final String fullName = "Automated Readability Index";
    final double DOUBLE0_5 = 0.5;
    final double DOUBLE4_71 = 4.71;
    final double DOUBLE21_43 = 21.43;

    ARI(TextStats textStats) {
        super(textStats);
    }

    @Override
    public double getScore() {
        double score = DOUBLE4_71 * textStats.getCharacters() / textStats.getWords()
                + DOUBLE0_5 * textStats.getWords() / textStats.getSentences()
                - DOUBLE21_43;
        return truncate(score);
    }

    @Override
    public String getFullName() {
        return fullName;
    }
}


class FK extends Score {
    public final String fullName = "Flesch–Kincaid readability tests";
    final double DOUBLE0_39 = 0.39;
    final double DOUBLE11_8 = 11.8;
    final double DOUBLE15_59 = 15.59;

    FK(TextStats textStats) {
        super(textStats);
    }

    @Override
    public double getScore() {
        double score = DOUBLE0_39 * textStats.getWords() / textStats.getSentences()
                + DOUBLE11_8 * textStats.getSyllables() / textStats.getWords()
                - DOUBLE15_59;
        return truncate(score);
    }

    @Override
    public String getFullName() {
        return fullName;
    }
}


class SMOG extends Score {
    public final String fullName = "Simple Measure of Gobbledygook";
    final double DOUBLE1_043 = 1.043;
    final double DOUBLE3_1291 = 3.1291;
    final double DOUBLE30 = 30;

    SMOG(TextStats textStats) {
        super(textStats);
    }

    @Override
    public double getScore() {
        double score = DOUBLE1_043 * Math.sqrt(textStats.getPolysyllables() * DOUBLE30 / textStats.getSentences())
                + DOUBLE3_1291;
        return truncate(score);
    }

    @Override
    public String getFullName() {
        return fullName;
    }
}


class CL extends Score {
    public final String fullName = "Coleman–Liau index";
    final double DOUBLE0_0588 = 0.0588;
    final double DOUBLE0_296 = 0.296;
    final double DOUBLE15_8 = 15.8;

    CL(TextStats textStats) {
        super(textStats);
    }

    @Override
    public double getScore() {
        double score = DOUBLE0_0588 * textStats.getCharacters() / textStats.getWords() * 100
                - DOUBLE0_296 * textStats.getSentences() / textStats.getWords() * 100
                - DOUBLE15_8;
        return truncate(score);
    }

    @Override
    public String getFullName() {
        return fullName;
    }
}
