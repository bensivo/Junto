package main.optransform;

import java.util.List;
import static main.optransform.Operation.TYPE.DELETE;
import static main.optransform.Operation.TYPE.INSERT;

public class Transformer {


    /**
     * The context of the OptContainer can change between Operation generation and application.
     * To solve this inconsistency, as new Operations are applied
     * to a container, Pending Operations are transformed to fit the new context. This usually just involves an index shift.
     *
     * @param pending The operation to be transformed.
     * @param applied The operation that has just been applied to the OptContainer, used to transform the pending one.
     */
    public static Operation transform(final Operation pending, final Operation applied){
        // A DEL causes all incoming ops to shift left, if they come after the DEL
        if(applied.type == DELETE){
            if(applied.index < (pending.index + pending.data.length())){
                int index = pending.index - applied.data.length();
                index = Math.max(0, index);
                return new Operation( pending.type, index, pending.data );
            }
        }

        // An INS causes all incoming ops to shift right, if they come after the INS
        else if(applied.type == INSERT && applied.index < pending.index){
            return new Operation(
                    pending.type,
                    pending.index + applied.data.length(),
                    pending.data
                );
        }

        return pending;
    }

    /**
     * Apply multiple transforms at once.
     * @param incoming
     * @param existing
     * @return
     */
    public static Operation transformMany(Operation incoming, List<Operation> existing ){
        Operation result = incoming;
        for(Operation applied: existing){
            result = transform(result, applied);
        }
        return result;
    }

}
