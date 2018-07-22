package main.optransform;

import java.util.Queue;

/**
 * Used to relay OpT events between OpT Containers.
 *
 * OpT containers 'subscribe' to a dispatcher, and the dispatcher
 * will send any remote changes to them.
 */
public class OpTDispatcher {
    private Queue q;
}
