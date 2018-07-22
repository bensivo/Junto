package test;

import main.optransform.OpQueue;
import main.optransform.Operation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpQueueTest {
    @Test
    void basicTest(){
        OpQueue opQueue = new OpQueue();
        opQueue.push(new Operation(Operation.TYPE.INSERT, 1, "Hello"));
        opQueue.push(new Operation(Operation.TYPE.DELETE, 0, "World"));

        Operation first = opQueue.pull();
        assertTrue(first.type == Operation.TYPE.INSERT );
        assertTrue(first.index == 1);
        assertTrue(first.data.equals("Hello"));

        Operation second = opQueue.pull();
        assertTrue(second.type == Operation.TYPE.DELETE );
        assertTrue(second.index == 0);
        assertTrue(second.data.equals("World"));
    }

}