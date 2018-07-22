package main.optransform;

import static main.optransform.Operation.TYPE.DELETE;
import static main.optransform.Operation.TYPE.INSERT;

/**
 * Applies Operations to Strings.
 */
public class Applicator {

    /**
     * Apply an operation to a string, returning the result.
     *
     * @param source String before operation
     * @param operation Operation to apply
     * @return Resulting string after operation.
     */
    public static String applyOperation(String source, Operation operation){
        if(operation.type == INSERT){
            StringBuilder builder = new StringBuilder(source);
            builder.insert(operation.index, operation.data);
            return builder.toString();
        }

        if(operation.type == DELETE){
            StringBuilder builder = new StringBuilder(source);
            for(int i=0; i<operation.data.length(); i++){
                builder.deleteCharAt(operation.index);
            }
            return builder.toString();
        }
        return source;
    }
}
