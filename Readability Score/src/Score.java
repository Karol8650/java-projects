
public abstract class Score {
    final int characters;
    final int sentences;
    final int words;
    final int syllables;
    final int polysyllables;
    final int precision;



    public Score(int characters, int sentences, int words, int syllables, int polysyllables, int precision) {
        this.characters = characters;
        this.sentences = sentences;
        this.words = words;
        this.syllables = syllables;
        this.polysyllables = polysyllables;
        this.precision = precision;
    }

    }

}


