package main.optransform;

import static main.optransform.Operation.TYPE.INSERT;

/**
 * An abstract representation of a text-container
 * that is linked to an Operational Transformation Service.
 *
 * Local changes in the container can be sent to an OpT Manager, and
 * remote changes coming from an OpT Manager can be applied to this container.
 */
public class OpTContainer {
    String content;

    public OpTContainer(){
        this.content = "";
    }

}
