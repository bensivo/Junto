package main;

import main.util.Diff;

public class TestMain {
    public static void main(String[] args) {
        Diff change1 = new Diff("srcID","abcdef", "abcghi");
        System.out.println(change1.toString());
        Diff change2 = new Diff("srcID","abbbbc", "abbbc");
        System.out.println(change2.toString());
        Diff change3 = new Diff("srcID","abc", "ab");
        System.out.println(change3.toString());
        Diff change4 = new Diff("srcID","abc", "abcdef");
        System.out.println(change4.toString());
        Diff change5 = new Diff("srcID","", "abcdef");
        System.out.println(change5.toString());
        Diff change6 = new Diff("srcID","abcdef", "");
        System.out.println(change6.toString());
    }


}
