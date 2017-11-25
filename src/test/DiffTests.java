package test;

import main.util.Diff;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DiffTests {

    @Test
    public void stringChangeDeleteOnlyCharacter(){
        Diff change = new Diff("srcID","a", "");
        assertTrue(change.getIndex() == 0);
        assertTrue(change.getDel().equals("a"));
        assertTrue(change.getAdd().equals(""));
    }

    @Test
    public void stringChangeDelAndAppend() {
        Diff change = new Diff("srcID","abcdef", "abcghi");
        assertTrue(change.getIndex() == 3);
        assertTrue(change.getDel().equals("def"));
        assertTrue(change.getAdd().equals("ghi"));
    }

    @Test
    public void stringChangeAppend() {
        Diff change = new Diff("srcID","abc", "abcdef");
        assertTrue(change.getIndex() == 3);
        assertTrue(change.getDel().equals(""));
        assertTrue(change.getAdd().equals("def"));
    }

    @Test
    public void stringChangeDeleteFromEnd(){
        Diff change = new Diff("srcID","abcdef", "abcde");
        assertTrue(change.getIndex() == 5);
        assertTrue(change.getDel().equals("f"));
        assertTrue(change.getAdd().equals(""));
    }

    @Test
    public void stringChangeDeleteFromBeg(){
        Diff change = new Diff("srcID","abcde", "bcde");
        assertTrue(change.getIndex() == 0);
        assertTrue(change.getDel().equals("a"));
        assertTrue(change.getAdd().equals(""));
    }

    @Test
    public void stringChangeDeleteRepeating(){
        Diff change = new Diff("srcID","abbbbc", "abbbc");
        assertTrue(change.getIndex() <= 4 && change.getIndex() >= 1);
        assertTrue(change.getDel().equals("b"));
        assertTrue(change.getAdd().equals(""));
    }
}
