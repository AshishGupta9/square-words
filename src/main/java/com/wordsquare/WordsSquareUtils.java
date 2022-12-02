package java.com.wordsquare;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class WordsSquareUtils {

    public static List<List<String>> findWordSquares(Set<String> words){
        if(words == null || words.size() == 0) throw new IllegalArgumentException("Word list cannot be null or empty!");

        WordMapper wordMapper = new WordMapper();
        Queue<Character[][]> incomplete = new LinkedList<>();

        //preprocess: add all words to our wordMapper and create a matrix initializing the first row and column to each single word
        for(String word : words){
            wordMapper.addWord(word);

            Character[][] square = new Character[word.length()][word.length()];

            for(int i = 0; i < word.length(); i++){
                square[0][i] = word.charAt(i);
                square[i][0] = word.charAt(i);
            }

            incomplete.add(square);
        }

        List<Character[][]> results = new LinkedList<>();

        //analyse each matrix trying to completely fill it
        while(!incomplete.isEmpty()){
            Character[][] square = incomplete.remove();
            //this will mark where the next incomplete word is. First word is always complete
            int i = 1;
            boolean isComplete = false;
            //track the word we built so far, so we can later search the wordMapper
            StringBuilder curr = new StringBuilder(square.length);

            //scan the current matrix
            for(; i < square.length; i++){
                //check if we completely filled it and in case, add it to the result set and process the next one
                if(i == square.length - 1 && square[i][i] != null){
                    results.add(square);
                    isComplete = true;
                }

                //otherwise we know which word is incomplete, build it up
                if(square[i][i] == null){
                    //at the very beginning the first character we need is the second in the only word we have
                    for(int j = 0; j < i; j++){
                        curr.append(square[i][j]);
                    }
                    break;
                }
            }

            //if we found a result, no need to keep processing this matrix
            if(isComplete) continue;

            //find all possible words with same prefix and length up to our current matrix
            List<String> possibilities = wordMapper.getSuggestions(curr.toString(), square.length);

            if(possibilities.isEmpty()) continue;

            //for each possibility, create a new matrix, add the new candidate word and place it back in the queue
            //the next round will either proceed further, find no other path to process, or verify we completely filled it
            for(String s : possibilities){
                Character[][] next = new Character[square.length][square.length];

                //COPY THE MATRIX! simply referencing again is not enough
                //and we need to create a new matrix from the current one for each candidate word!
                for(int row = 0; row < square.length; row++){
                    for(int column = 0; column < square.length; column++) {
                        next[row][column] = square[row][column];
                    }
                }

                //add the candidate word starting from the place we know is incomplete at the moment
                for(int k = 0; k < s.length(); k++){
                    next[i][k] = s.charAt(k);
                    next[k][i] = s.charAt(k);
                }

                incomplete.add(next);
            }
        }

        //collect result from the complete matrices, if any
        List<List<String>> out = new LinkedList<>();

        for(int i = 0; i < results.size(); i++){
            Character[][] curr = results.get(i);

            List<String> answers = new LinkedList<>();
            StringBuilder word;

            //get all words from this matrix and add them to the corresponding result set
            for(int j = 0; j < curr.length; j++){
                word = new StringBuilder(curr.length);

                for(int k = 0; k < curr.length; k++){
                    word.append(curr[j][k]);
                }

                answers.add(word.toString());
            }

            out.add(answers);
        }

        return out;
    }

    private static String getAllowedRegex(String wordCharacters, Integer wordsLength){
        StringBuilder sb = new StringBuilder();
        sb.append("^[");
        wordCharacters.chars().mapToObj(e -> Character.toString((char) e))
                .distinct().forEach(sb::append);
        sb.append("]{");
        sb.append(wordsLength);
        sb.append("}");
        return sb.toString();
    }

    public static Set<String> getWordsListFromFile(String wordCharacters, Integer wordsLength) {

        String allowedRegex = getAllowedRegex(wordCharacters, wordsLength);

        File file = new File("src/main/resources/words.txt");
        Set<String> stringSet = new HashSet<>();

        String line;
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            while((line = br.readLine()) != null){
                if(line.matches(allowedRegex)){
                    stringSet.add(line);
                }
            }
        }catch(Exception e){
            System.out.println("Error occurred - Logger could be implemented here: " + e);
        }
        return stringSet;
    }
}
