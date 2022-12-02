package java.com.wordsquare;

import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String args) {

        Character number_char = args.charAt(0);
        String alphabets = args.substring(2);

        Set<String> wordsList = WordsSquareUtils.getWordsListFromFile(alphabets,
                Character.getNumericValue(number_char));
        List<List<String>> wordSquares = WordsSquareUtils.findWordSquares(wordsList);

        for (List<String> squareWordsList: wordSquares) {
            System.out.println("Words Squares Start");
            for (String word: squareWordsList) {
                System.out.println(word);
            }
            System.out.println("Words Squares End");
        }
    }
}