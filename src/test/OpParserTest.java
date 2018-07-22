package test;

import main.optransform.OpParser;
import main.optransform.Operation;
import org.junit.jupiter.api.Test;

import static main.optransform.Operation.TYPE.DELETE;
import static main.optransform.Operation.TYPE.INSERT;
import static org.junit.jupiter.api.Assertions.*;

public class OpParserTest {
    @Test
    void singleInsert(){
        String pre = "1234567890";
        String post = "123X4567890";

        Operation expected = new Operation(INSERT, 3, "X");
        Operation generated = OpParser.getOperations(pre, post).get(0);

        assertTrue(generated.equals(expected));
    }

    @Test
    void multiInsert(){
        String pre = "1234567890";
        String post = "123456ABC7890";

        Operation expected = new Operation(INSERT, 6, "ABC");
        Operation generated = OpParser.getOperations(pre, post).get(0);

        assertTrue(generated.equals(expected));
    }

    @Test
    void singleDelete(){
        String pre = "1234567890";
        String post = "134567890";

        Operation expected = new Operation(DELETE, 1, "2");
        Operation generated = OpParser.getOperations(pre, post).get(0);

        assertTrue(generated.equals(expected));
    }

    @Test
    void multiDelete(){
        String pre = "1234567890";
        String post = "1234590";

        Operation expected = new Operation(DELETE, 5, "678");
        Operation generated = OpParser.getOperations(pre, post).get(0);

        assertTrue(generated.equals(expected));
    }

    @Test
    void deleteAll(){
        String pre = "1234567890";
        String post = "";

        Operation expected = new Operation(DELETE, 0, "1234567890");
        Operation generated = OpParser.getOperations(pre, post).get(0);

        assertTrue(generated.equals(expected));
    }

    @Test
    void addAll(){
        String pre = "";
        String post = "1234567890";

        Operation expected = new Operation(INSERT, 0, "1234567890");
        Operation generated = OpParser.getOperations(pre, post).get(0);

        assertTrue(generated.equals(expected));
    }

    @Test
    void noChange(){
        String pre = "1234567890";
        String post = "1234567890";

        assertTrue(OpParser.getOperations(pre, post).size() == 0);
    }
}
