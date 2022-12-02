package java.com.wordsquare.test;

import org.junit.Test;

import java.com.wordsquare.WordsSquareUtils;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class TestWordsSquare {
    //empty input, expected exception
    List<List<String>> out;
    Set<String> in;

    @Test
    public void empty() {
        in = null;

        try {
            out = WordsSquareUtils.findWordSquares(in);
        } catch (IllegalArgumentException e) {
            System.out.println("null list got IllegalArgumentException " + e.getMessage());
        }

        in = new HashSet<>();

        try {
            out = WordsSquareUtils.findWordSquares(in);
        } catch (IllegalArgumentException e) {
            System.out.println("empty list got IllegalArgumentException " + e.getMessage());
        }
    }

    @Test
    public void noSquare() {
        in = new HashSet<>();
        in.add("area");
        in.add("ball");
        in.add("dear");
        in.add("lead");
        in.add("yard");

        out = WordsSquareUtils.findWordSquares(in);

        assertEquals("square can't be built, got empty result", true, out.isEmpty());
    }

    @Test
    public void sameLengthSquare() {
        in = new HashSet<>();
        in.add("area");
        in.add("ball");
        in.add("dear");
        in.add("lady");
        in.add("lead");
        in.add("yard");

        out = WordsSquareUtils.findWordSquares(in);

        assertEquals("two squares can be built", 2, out.size());

        List<String> square1 = new LinkedList<>();
        square1.add("ball");
        square1.add("area");
        square1.add("lead");
        square1.add("lady");

        assertThat("first square is ball, area, lead, lady", out.get(0), is(square1));

        List<String> square2 = new LinkedList<>();
        square2.add("lady");
        square2.add("area");
        square2.add("dear");
        square2.add("yard");

        assertThat("second square is lady, area, dear, yard", out.get(1), is(square2));
    }
}