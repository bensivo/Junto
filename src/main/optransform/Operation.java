package main.optransform;

import java.io.Serializable;


/**
 * In the context of OptTransform, an Operation is a single edit made on a page.
 * Similar to the "Diff" class also listed here. However, an Operation is simpler, containing only
 * inserts and deletes. There is no "replace" operation. Operations also do not carry source information.
 *
 * Note: Operations are immutable once created.
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

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if((obj == null) || (obj.getClass() != this.getClass()))
            return false;
        // object must be Test at this point
        Operation op = (Operation)obj;
        return this.type == op.type && this.index == op.index && this.data.equals(op.data);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 3 * hash + index;
        hash = 4 * hash + (null == data ? 0 : data.hashCode());
        hash = 5 * hash + (type == TYPE.INSERT ? 11 : 13);
        return hash;
    }

    @Override
    public String toString(){
        String res = "{";
        res += "type:" + (type==TYPE.DELETE ? "Delete" : "Insert") + ",";
        res += "index:" + String.valueOf(index) + ",";
        res += "data:" + data;
        res += "}";
        return res;


    }
}
