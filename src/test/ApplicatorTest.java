package test;

import main.optransform.Applicator;
import main.optransform.Operation;
import org.junit.jupiter.api.Test;

import static main.optransform.Operation.TYPE.DELETE;
import static main.optransform.Operation.TYPE.INSERT;
import static org.junit.jupiter.api.Assertions.*;

class ApplicatorTest {

    @Test
    void insert_SingleChar() {
        String res = Applicator.applyOperation("1234567890", new Operation(INSERT, 2, "X"));
        assertTrue(res.equals("12X34567890"));

    }

    @Test
    void insert_MultiChar() {
        String res =  Applicator.applyOperation("1234567890", new Operation(INSERT, 2, "ABC"));
        assertTrue(res.equals("12ABC34567890"));
    }

    @Test
    void insert_NegativeIndex_ThrowsOutOfBounds(){
        assertThrows(StringIndexOutOfBoundsException.class, ()->{
            Applicator.applyOperation("1234567890", new Operation(INSERT, -1, "ABC"));
        });
    }

    @Test
    void insert_IndexOverlow_ThrowsOutOfBounds(){
        assertThrows(StringIndexOutOfBoundsException.class, ()->{
            Applicator.applyOperation("1234567890", new Operation(INSERT, 20, "ABC"));
        });
    }

    @Test
    void delete_SingleChar() {
        String res = Applicator.applyOperation("1234567890", new Operation(DELETE, 2, "X"));
        assertTrue(res.equals("124567890"));

    }

    @Test
    void delete_MultiChar() {
        String res =  Applicator.applyOperation("1234567890", new Operation(DELETE, 2, "ABC"));
        assertTrue(res.equals("1267890"));
    }

    @Test
    void delete_NegativeIndex_ThrowsOutOfBounds(){
        assertThrows(StringIndexOutOfBoundsException.class, ()->{
            Applicator.applyOperation("1234567890", new Operation(DELETE, -1, "ABC"));
        });
    }

    @Test
    void delete_IndexOverlow_ThrowsOutOfBounds(){
        assertThrows(StringIndexOutOfBoundsException.class, ()->{
            Applicator.applyOperation("1234567890", new Operation(DELETE, 20, "ABC"));
        });
    }
}