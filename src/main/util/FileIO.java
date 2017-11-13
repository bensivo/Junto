package main.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileIO {

    public static byte[] readFile(String path) throws IOException {

        byte[] bytes = Files.readAllBytes(Paths.get(path));
        return bytes;
    }

    public static List<byte[]> readFiles(List<String> path) throws IOException {

        List<byte[]> outList = new ArrayList<byte[]>();

        for (String file : path) {
            outList.add(readFile(file));
        }

        return outList;
    }

    public static boolean writeFile(String path, byte[] data) {

        FileOutputStream out = null;
        try {
        //write file here
        File file = new File(path);
        out = new FileOutputStream(file);
        out.write(data);
        out.flush();
        out.close();

        //if file doesnt write correctly return false
        } catch(IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
  
}

