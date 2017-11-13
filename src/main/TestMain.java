package main;

import main.util.StringChange;

public class TestMain {
    public static void main(String[] args) {
        StringChange change1 = new StringChange("abcdef", "abcghi");
        System.out.println(change1.toString());
        StringChange change2 = new StringChange("abbbbc", "abbbc");
        System.out.println(change2.toString());
        StringChange change3 = new StringChange("abc", "ab");
        System.out.println(change3.toString());
        StringChange change4 = new StringChange("abc", "abcdef");
        System.out.println(change4.toString());
        StringChange change5 = new StringChange("", "abcdef");
        System.out.println(change5.toString());
        StringChange change6 = new StringChange("abcdef", "");
        System.out.println(change6.toString());
    }


}
