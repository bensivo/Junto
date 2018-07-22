package test;

import main.optransform.Operation;
import main.optransform.OpTransformer;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static main.optransform.Operation.TYPE.DELETE;
import static main.optransform.Operation.TYPE.INSERT;
import static org.junit.jupiter.api.Assertions.*;

class OpTransformerTest {

    @Test
    void deleteBeforeIncoming() {
        Operation del = new Operation(DELETE, 0, "x");
        Operation incoming = new Operation(Operation.TYPE.INSERT, 12, "Hello");

        Operation nIncoming = OpTransformer.transform(incoming, del);
        assertTrue(nIncoming.index == 11);

        Operation del3 = new Operation(DELETE, 0, "xxx");
        incoming = new Operation(Operation.TYPE.INSERT, 12, "Hello");
        nIncoming = OpTransformer.transform(incoming, del3);
        assertTrue(nIncoming.index == 9);
    }

    @Test
    void incomingAfterApplied_DoesNothing(){
        Operation del, ins, incoming, nIncoming;

        del = new Operation(DELETE, 20, "xxx");
        incoming = new Operation(INSERT, 12, "Hello");
        nIncoming = OpTransformer.transform(incoming, del);
        assertTrue(nIncoming.index == 12);


        ins = new Operation(INSERT, 20, "xxx");
        incoming = new Operation(INSERT, 12, "Hello");
        nIncoming = OpTransformer.transform(incoming, ins);
        assertTrue(nIncoming.index == 12);
    }

    @Test
    void insertBeforeIncoming(){
        Operation ins, incoming, nIncoming;

        ins = new Operation(INSERT, 0, "x");
        incoming = new Operation(INSERT, 15, "hello");
        nIncoming = OpTransformer.transform(incoming, ins);
        assertTrue(nIncoming.index == 16);

        ins = new Operation(INSERT, 0, "xxx");
        incoming = new Operation(INSERT, 15, "hello");
        nIncoming = OpTransformer.transform(incoming, ins);
        assertTrue(nIncoming.index == 18);
    }

    @Test
    void transformMany() {
        Operation incoming = new Operation(INSERT, 10, "hello");

        Operation[] applied = new Operation[]{
            new Operation(DELETE, 0, "x"), //index--
            new Operation(INSERT, 10, "xxx"), //no change
            new Operation(INSERT, 3, "xx"), //index + 2
        };


        Operation newIncoming = OpTransformer.transformMany(incoming, Arrays.asList(applied));
        assertTrue(newIncoming.index == 11);
    }

    //If you delete over the incoming operation, it won't go negative.
    @Test
    void delOverIncoming_noNegativeValues() {
        Operation ins, incoming, nIncoming;

        ins = new Operation(DELETE, 0, "1234567890");
        incoming = new Operation(INSERT, 5, "hello");
        nIncoming = OpTransformer.transform(incoming, ins);
        assertTrue(nIncoming.index == 0);
    }
}