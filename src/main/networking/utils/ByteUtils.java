package main.networking.utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Utility functions for converting objects into byte arrays, and back again.
 * Also contains functions to assist in socket communication
 */
public class ByteUtils {

    /**
     * Create a byte array representation of a Serializable object
     *
     * @param obj The object to convert to a byte array
     * @return A byte Array that can be used to reconstruct the original object
     * @throws IOException If the object cannot be converted to a byte array
     */
    public static byte[] toBytes(Object obj) throws IOException{
        try{
            //Turn the header object into a byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutput output = new ObjectOutputStream(baos);
            output.writeObject(obj);
            output.flush();
            byte[] headerBytes = baos.toByteArray();

            return headerBytes;
        }catch(IOException ioe){
            throw ioe;
        }
    }

    /**
     * Create a byte array representation of a Serializable object, with the length prepended
     * as the first 8 bytes (a long) of the array
     *
     * @param obj The object to convert to a byte array
     * @return A byte Array that can be used to reconstruct the original object, with the first 8 bytes denoting the length of the rest
     * @throws IOException If the object cannot be converted to a byte array
     */
    public static byte[] toBytesWithLength(Object obj) throws IOException{
        try{
            //Get the header object as a byte array
            byte[] headerBytes = ByteUtils.toBytes(obj);
            byte[] sizePacket = ByteUtils.createSizePacket(headerBytes);

            //Concatenate the two byte arrays
            return ByteUtils.concatenate(sizePacket, headerBytes);
        }catch(IOException ioe){
            throw ioe;
        }
    }

    /**
     * Recreates an Object from a byte array
     *
     * Code from: https://stackoverflow.com/questions/2836646/java-serializable-object-to-byte-array
     *
     * @param bytes The byte array to construct a Header object from. Created with toBytes()
     * @return  An object identical to the one used to create the byte array
     * @throws IOException On unsuccessful reading of bytes
     * @throws ClassNotFoundException On unsuccessfull converestion of byte result to Object
     */
    public static synchronized Object fromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInput input = null;
        try{
            input = new ObjectInputStream(bais);
            Object obj = input.readObject();
            return obj;
        }catch(IOException ioe){
            throw ioe;
        }catch(ClassNotFoundException e){
            throw e;
        }
    }

    /**
     * Create an 8 byte packet that stores the size of any byte array.
     * Size packets are used in socket communication, to notify the recieving end
     * about the size of the upcoming packet
     *
     * @param bytes The original byte array to create a size packet for                        chatClient.stop();
     * @return
     */
    public static byte[] createSizePacket(byte[] bytes){
        ByteBuffer byteBuffer = ByteBuffer.allocate(Long.BYTES);
        byteBuffer.putLong((long)bytes.length);

        return byteBuffer.array();
    }

    /**
     * Concatenate two byte arrays, as if they were strings
     * @param a first byte array
     * @param b second byte array
     * @return a+b
     */
    public static byte[] concatenate(byte[] a, byte[] b){
        byte[] returnBytes = new byte[a.length + b.length];
        System.arraycopy(a, 0, returnBytes, 0, a.length);
        System.arraycopy(b, 0, returnBytes, a.length, b.length);
        return returnBytes;
    }

    public static long readSizePacket(byte[] bytes){
        ByteBuffer bb = ByteBuffer.allocate(Long.BYTES);
        bb.put(bytes, 0, Long.BYTES);
        return bb.getLong(0);
    }

    public static byte[] removeSizePacket(byte[] bytes){
        return Arrays.copyOfRange(bytes, Long.BYTES, bytes.length);
    }

}

