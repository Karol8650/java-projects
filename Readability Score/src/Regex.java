public class Regex {
    static String characters = "\s";

    static String words = "[\\w-?.!,\\d]+";

    static String sentences = "[?.!]+\\s*";

    static String syllables = "\\b[^aeyuioAEYUIO\\s]+[aeyuioAEYUIO]\\b" + // matches whole word starting with one or more consonants
            // and ending with one vowel, there is nothing in between (e.g. "the")
            "|[aeyuioAEYUIO]*[eE](?!\\b)[aeyuioAEYUIO]*" + // matches series of vowels containing 'e' or 'E',
            // but not 'e' or 'E' at the end of word
            "|[ayuioAYUIO]+" + // matches one or more vowels except for "eE"
            "|\\d+[,.]\\d*"; // matches numbers like 142,262
}