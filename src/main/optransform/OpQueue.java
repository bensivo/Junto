package main.optransform;

import java.util.LinkedList;
import java.util.Queue;

/**
 * An Operation Queue (or OpQueue for short), is an event queue for holding
 * either incoming or outgoing operations, pre-transformation.
 */
public class OpQueue {
    Queue<Operation> queue;

    public OpQueue(){
        this.queue = new LinkedList<>();
    }

    public synchronized boolean push(Operation op){
        return this.queue.add(op);
    }

    public synchronized Operation pull(){
        return this.queue.poll();
    }

    public int size(){
        return this.queue.size();
    }

}
