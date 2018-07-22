package main.fileio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Simple File operations
 */
public class FileIO {

    /**
     * Write a file to the filesystem
     * @param path Where the file will be stored
     * @param data File contents
     * @return True if file written successfully
     */
    public static boolean writeFile(String path, byte[] data) {
        try {
            File file = new File(path);
            FileOutputStream out = new FileOutputStream(file);
            out.write(data);
            out.flush();
            out.close();
            return true;
        } catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Write a file to the filesystem
     * @param path Where the file will be stored
     * @param data File contents
     * @return True if file written successfully
     */
    public static boolean writeFile(String path, String data){
        return writeFile(path, data.getBytes());
    }

    /**
     * Read a file from the filesystem
     * @param path
     * @return
     * @throws IOException
     */
    public static byte[] readFile(String path) throws IOException {

        byte[] bytes = Files.readAllBytes(Paths.get(path));
        return bytes;
    }
}
