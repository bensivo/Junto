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
    public enum TYPE {
        INSERT,
        DELETE,
    }

    public final TYPE type;
    public final int index;
    public final String data;
    public String source;  // Source is usually filename, but could be "Untitled 1"
    // source is also not final, the case it needs to be updated by a router mid-transit


    public Operation(TYPE type, int index, String data, String source){
        this.type = type;
        this.index = index;
        this.data = data;
        this.source = source;
    }

    public Operation(TYPE type, int index, String data){
        this(type, index, data, "");
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if((obj == null) || (obj.getClass() != this.getClass()))
            return false;
        Operation op = (Operation)obj;
        return this.type == op.type && this.index == op.index && this.data.equals(op.data) && this.source.equals(op.source);
    }

    /**
     * Hashes by multiplying be a series of prime numbers
     */
    @Override
    public int hashCode() {
        int hash = 19;
        hash = 2 * hash + index;
        hash = 3 * hash + (null == data ? 0 : data.hashCode());
        hash = 5 * hash + (null == source? 0 : source.hashCode());
        hash = 7 * hash + (type == TYPE.INSERT ? 11 : 13);
        return hash;
    }

    @Override
    public String toString(){
        String res = "{";
        res += "type:" + (type==TYPE.DELETE ? "Delete" : "Insert") + ", ";
        res += "index:" + String.valueOf(index) + ", ";
        res += "data:" + data + ", ";
        res += "source:" + source;
        res += "}";
        return res;
    }
}
