package main.util;

import java.io.File;

public class FilePathUtils {

    public static String getHomeDir(){
        return System.getProperty("user.home");
    }

    public static String getCloud10Dir(){
        if(isWindows()){
            return getHomeDir() + "\\Cloud10";
        }
        else{
            return getHomeDir() + "/Cloud10";
        }
    }

    public static boolean isWindows(){
        if(System.getProperty("os.name").startsWith("Windows")){
            return true;
        }
        else{
            return false;
        }
    }

    public static String getFilePathSeparator(){
        if(isWindows()){
            return "\\";
        }
        else{
            return "/";
        }
    }

    public static void createCloud10DirIfNotCreated(){
        new File(getCloud10Dir()).mkdirs();
    }

}
