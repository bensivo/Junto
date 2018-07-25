package main.util;

import java.io.File;

/**
 * Static util classes for resolving and generating filepaths across multiple platforms.
 */
public class FilePathUtils {

    public static String getHomeDir(){
        return System.getProperty("user.home");
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

}
