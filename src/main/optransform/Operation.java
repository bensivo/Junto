package main.optransform;

import java.io.Serializable;

/**
 * In the context of OptTransform, an Operation is a single edit made on a page.
 * Similar to the "Diff" class also listed here. However, an Operation is simpler, containing only
 * inserts and deletes. There is no "replace" operation. Operations also do not carry source information.
 */
public class Operation implements Serializable {
    public static enum TYPE {
        INSERT,
        DELETE
    }

    public final TYPE type;
    public final int index;
    public final String data;

    public Operation(TYPE type, int index, String data){
        this.type = type;
        this.index = index;
        this.data = data;
    }
}
