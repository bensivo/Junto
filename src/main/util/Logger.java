package main.util;

public class Logger {
    public static void logI(String tag, String message){
        System.out.println("logI::"+tag+"::"+message);
    }

    public static void logD(String tag, String message){
        System.out.println("logD::"+tag+"::"+message);
    }

    public static void logE(String tag, String message){
        System.out.println("logE::"+tag+"::"+message);
    }
}
