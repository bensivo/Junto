package main.optransform;

import java.util.List;

import static main.optransform.Operation.TYPE.DELETE;
import static main.optransform.Operation.TYPE.INSERT;

/**
 * Static methods for applying operations to Strings. This assumes that the operations have already been transformed
 * to exist in the same context as the string.
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

    /**
     * Apply several operations to a string at once. Does NOT transform pending operations
     * as new ones are applied.
     * @param source
     * @param operations
     * @return
     */
    public static String applyOperations(String source, List<Operation> operations){
        String result = source;
        for(Operation op: operations){
            result = applyOperation(result, op);
        }
        return result;
    }
}
