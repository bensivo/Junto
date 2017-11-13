package test;

import main.util.StringChange;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringUtilsTest {

    @Test
    public void stringChangeTest(){
        StringChange change1 = new StringChange("abcdef", "abcde");
        assertTrue(change1.getIndex() == 5);
        assertTrue(change1.getDel().equals("f"));
        assertTrue(change1.getAdd().equals(""));

        StringChange change2 = new StringChange("abbbbc", "abbbc");
        assertTrue(change2.getIndex() <= 4 && change2.getIndex() >= 1);
        assertTrue(change2.getDel().equals("b"));
        assertTrue(change2.getAdd().equals(""));

        StringChange change4 = new StringChange("abc", "abcdef");
        assertTrue(change4.getIndex() == 3);
        assertTrue(change4.getDel().equals(""));
        assertTrue(change4.getAdd().equals("def"));

        StringChange change5 = new StringChange("abcdef", "abcghi");
        assertTrue(change5.getIndex() == 3);
        assertTrue(change5.getDel().equals("def"));
        assertTrue(change5.getAdd().equals("ghi"));
    }
}
