package windowing;

import windowing.datastructures.CompositeNumber;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

public class CompositeNumberTest {
    
    @Test
    public void compare() {
        CompositeNumber cnumber1 = new CompositeNumber(2.0, 3.0);
        CompositeNumber cnumber2 = new CompositeNumber(1.0, 9.0);
        CompositeNumber cnumber3 = new CompositeNumber(2.0, 9.0);
        
        Assertions.assertEquals(cnumber1.is_x_smaller_than(cnumber2), false);
        Assertions.assertEquals(cnumber1.is_x_smaller_than(cnumber3), true);
        Assertions.assertEquals(cnumber3.is_x_smaller_than(cnumber2), false);
    }
}
