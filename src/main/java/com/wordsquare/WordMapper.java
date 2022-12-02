package java.com.wordsquare;

import java.util.*;

public class WordMapper {

    private String word;
    private String path;
    private HashMap<Character, WordMapper> next;
    private Set<String> words;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HashMap<Character, WordMapper> getNext() {
        return next;
    }

    public void setNext(HashMap<Character, WordMapper> next) {
        this.next = next;
    }

    public Set<String> getWords() {
        return words;
    }

    public void setWords(Set<String> words) {
        this.words = words;
    }

    public WordMapper() {
        this.word = null;
        this.path = null;
        this.next = new HashMap<>();
        this.words = new HashSet<>();
    }

    public WordMapper(String word) {
        this.word = word;
        this.path = null;
        this.next = new HashMap<>();
        this.words = new HashSet<>();
        this.words.add(word);
    }

    public WordMapper(String path, Character link) {
        this.word = null;
        this.path = path + link;
        this.next = new HashMap<>();
        this.words = new HashSet<>();
    }

    private void addWord(String word, String curr){
        Character c = curr.charAt(0);

        WordMapper t = this.next.get(c);

        /*
        if this is the last letter in the word
        add the full word alongside this letter creating a new node if necessary
        otherwise move to the next character creating a new node if necessary
        */
        if(curr.length() == 1){
            if(t == null) this.next.put(c, new WordMapper(word));//new node has a word
            else {
                t.word = word;
                t.words.add(word);
            }
        }
        else{
            //create the node before continuing!
            if(t == null){
                t = new WordMapper(this.path, c);
                this.next.put(c, t); //new node has no word
            }
            t.addWord(word, curr.substring(1)); //call add on the next character in the sequence
        }
    }

    //to add a word we walk the tree and create nodes as necessary until we reach the end of the word
    public void addWord(String word){
        if(word == null || word.equals(""))return;
        this.addWord(word, word);
    }

    public WordMapper findWord(String word){
        if(word == null || word.equals(""))return null;

        WordMapper t = this.next.get(word.charAt(0));

        /*
        if we have no entry, the word is not there, return Trie t (null)
        else if we have an entry and we are at the end of the word, return Trie t (input match)
        else keep searching for the next word portion
        */
        if(t == null) return t;
        else if(word.length() == 1) return t;
        else return t.findWord(word.substring(1));
    }

    public List<String> getSuggestions(String partial_word, int maxLength) {
        if (partial_word == null || partial_word.equals("")) return new ArrayList<>();

        //find the lowest subtree for this partial word, if any
        WordMapper t = findWord(partial_word);

        //if there is no subtree or it has no continuation, there is no suggestion
        if (t == null || t.next.keySet().isEmpty()) return new ArrayList<>();

        //otherwise search for all suggestions and store them in the List before returning it
        List<String> suggestions = new ArrayList<>();

        Queue<WordMapper> q = new ArrayDeque<>();
        q.addAll(t.next.values());

        while (!q.isEmpty()) {
            t = q.remove();
            if (t.word != null && t.word.length() <= maxLength) suggestions.add(t.word);
            if (!t.next.isEmpty()) {
                q.addAll(t.next.values());
            }
        }

        return suggestions;
    }
}
