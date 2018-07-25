package test;

import main.networking.core.DataPacket;
import main.networking.utils.ByteUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static main.networking.core.DataPacket.TYPE_OP;
import static org.junit.jupiter.api.Assertions.*;

class Apples implements Serializable {
    public final String type;
    public final int number;

    public Apples(String t, int n){
        this.type = t;
        this.number = n;
    }
}

class ByteUtilsTest {

    @Test
    void object_toBytes_fromBytes(){
        try {
            Apples apples = new Apples("Granny Smith", 10);
            byte[] bytes = ByteUtils.toBytes(apples);
            Apples nApples = (Apples)ByteUtils.fromBytes(bytes);
            assertEquals(nApples.type, "Granny Smith");
            assertEquals(nApples.number, 10);

        }catch(IOException ioe){
            ioe.printStackTrace();
            fail("IOException in Byteutils: " + ioe.getLocalizedMessage());
        }
        catch (ClassNotFoundException cnfe){
            cnfe.printStackTrace();
            fail("Classnotfound in Byteutils: " + cnfe.getLocalizedMessage());

        }
    }

    @Test
    void dataPacket_toBytes_fromBytes(){
        try {
            // Create apples
            Apples apples = new Apples("Granny Smith", 10);
            byte[] appleBytes = ByteUtils.toBytes(apples);

            // Create meta
            Map<String,Object> meta = new HashMap<>();
            meta.put("source", "appleFarm");

            // Create dp
            DataPacket dp = new DataPacket(appleBytes, TYPE_OP, meta);

            // To bytes
            byte[] dpBytes = ByteUtils.toBytes(dp);

            // Parse DP
            DataPacket ndp = (DataPacket) ByteUtils.fromBytes(dpBytes);

            // Check meta and type
            assertTrue((ndp.meta.get("source")).equals("appleFarm"));
            assertEquals(ndp.type, TYPE_OP);

            // Parse Apples
            Apples nApples = (Apples)ByteUtils.fromBytes(ndp.data);

            // check apples data
            assertEquals(nApples.type, "Granny Smith");
            assertEquals(nApples.number, 10);

        }catch(IOException ioe){
            ioe.printStackTrace();
            fail("IOException in Byteutils: " + ioe.getLocalizedMessage());
        }
        catch (ClassNotFoundException cnfe){
            cnfe.printStackTrace();
            fail("Classnotfound in Byteutils: " + cnfe.getLocalizedMessage());
        }
    }

    @Test
    void adding_removing_size_packet(){
        try {
            // Create apples
            Apples apples = new Apples("Granny Smith", 10);
            byte[] appleBytes = ByteUtils.toBytesWithLength(apples);

            long size = ByteUtils.readSizePacket(appleBytes);
            assertNotEquals(size, 0);

            byte[] nAppleBytes = ByteUtils.removeSizePacket(appleBytes);

            assertEquals(nAppleBytes.length, size); //check the size was correct

            Apples nApples = (Apples)ByteUtils.fromBytes(nAppleBytes);
            assertEquals(nApples.type, "Granny Smith");
            assertEquals(nApples.number, 10);


        }catch(IOException ioe){
            ioe.printStackTrace();
            fail("IOException in Byteutils: " + ioe.getLocalizedMessage());
        }
        catch (ClassNotFoundException cnfe){
            cnfe.printStackTrace();
            fail("Classnotfound in Byteutils: " + cnfe.getLocalizedMessage());
        }

    }

}