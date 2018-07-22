package main.optransform;

import java.util.ArrayList;
import java.util.List;

import static main.optransform.Operation.TYPE.DELETE;
import static main.optransform.Operation.TYPE.INSERT;

/**
 * Take strings and produce operation that map one string to another.
 */
public class OpParser {

    /**
     * Given a before and after string, generates either no, one, or two Operations
     * that can be used to go from the before to the after.
     *
     * NOTE: If the 2 strings differ by MORE than a single atomic edit, it will be impossible to determine.
     *      - 2 operations result from selecting text, and hitting "Paste", resulting in a delete and an insert at the same index.
     *
     * @param original
     * @param revised
     * @return
     */
    public static List<Operation> getOperations(String original, String revised){
        List<Operation> operations = new ArrayList<>();

        if(original.equals(revised)){
            return operations;
        }

        if(original.equals("")){
            operations.add(new Operation(INSERT, 0, revised));
            return operations;
        }

        if(revised.equals("")){
            operations.add(new Operation(DELETE, 0, original));
            return operations;
        }

        //Iterate from the start of both strings until they no longer match
        int diffBeg;
        for (diffBeg = 0; diffBeg < original.length() && diffBeg < revised.length(); ++diffBeg) {
            if (original.charAt(diffBeg) != revised.charAt(diffBeg)) {
                break;
            }
        }

        //Iterate from the end of both strings until they no longer match
        int j;
        int diffEndOriginal = 0;
        int diffEndRevised = 0;
        for (j = 0; j < original.length() && j < revised.length(); ++j) {
            if (original.charAt(original.length()-1-j) != revised.charAt(revised.length()-1-j)) {
                break;
            }
            if(original.length()-j <= diffBeg || revised.length()-j <= diffBeg){
                break;
            }
        }

        //The diff is everything that was in the middle of the two indexes
        diffEndOriginal = original.length()-j;
        diffEndRevised = revised.length()-j;
        String del = original.substring(diffBeg, diffEndOriginal);
        String add = revised.substring(diffBeg, diffEndRevised);
        int index = diffBeg;

        if(del.length() > 0){
            operations.add(new Operation(DELETE, index, del));
        }

        if(add.length() > 0){
            operations.add(new Operation(INSERT, index, add));
        }
        return operations;
    }
}
