package test;

import main.util.StringChange;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringChangeTests {

    @Test
    public void stringChangeDeleteOnlyCharacter(){
        StringChange change = new StringChange("a", "");
        assertTrue(change.getIndex() == 0);
        assertTrue(change.getDel().equals("a"));
        assertTrue(change.getAdd().equals(""));
    }

    @Test
    public void stringChangeDelAndAppend() {
        StringChange change = new StringChange("abcdef", "abcghi");
        assertTrue(change.getIndex() == 3);
        assertTrue(change.getDel().equals("def"));
        assertTrue(change.getAdd().equals("ghi"));
    }

    @Test
    public void stringChangeAppend() {
        StringChange change = new StringChange("abc", "abcdef");
        assertTrue(change.getIndex() == 3);
        assertTrue(change.getDel().equals(""));
        assertTrue(change.getAdd().equals("def"));
    }

    @Test
    public void stringChangeDeleteFromEnd(){
        StringChange change = new StringChange("abcdef", "abcde");
        assertTrue(change.getIndex() == 5);
        assertTrue(change.getDel().equals("f"));
        assertTrue(change.getAdd().equals(""));
    }

    @Test
    public void stringChangeDeleteFromBeg(){
        StringChange change = new StringChange("abcde", "bcde");
        assertTrue(change.getIndex() == 0);
        assertTrue(change.getDel().equals("a"));
        assertTrue(change.getAdd().equals(""));
    }

    @Test
    public void stringChangeDeleteRepeating(){
        StringChange change = new StringChange("abbbbc", "abbbc");
        assertTrue(change.getIndex() <= 4 && change.getIndex() >= 1);
        assertTrue(change.getDel().equals("b"));
        assertTrue(change.getAdd().equals(""));
    }
}
