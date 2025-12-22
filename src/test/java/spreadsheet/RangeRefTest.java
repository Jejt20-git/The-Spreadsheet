package spreadsheet;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class RangeRefTest {

    @Test
    void expandsRowMajorRectangleInclusive() {
        RangeRef r = new RangeRef(new Coordinate(1, 1), new Coordinate(2, 3));
        List<Coordinate> coords = r.expand();

        assertEquals(6, coords.size());
        assertEquals(new Coordinate(1, 1), coords.get(0));
        assertEquals(new Coordinate(1, 2), coords.get(1));
        assertEquals(new Coordinate(1, 3), coords.get(2));
        assertEquals(new Coordinate(2, 1), coords.get(3));
        assertEquals(new Coordinate(2, 2), coords.get(4));
        assertEquals(new Coordinate(2, 3), coords.get(5));
    }

    @Test
    void expandsEvenIfStartEndReversed() {
        RangeRef r = new RangeRef(new Coordinate(2, 3), new Coordinate(1, 1));
        List<Coordinate> coords = r.expand();
        assertEquals(6, coords.size());
        assertEquals(new Coordinate(1, 1), coords.get(0));
        assertEquals(new Coordinate(2, 3), coords.get(5));
    }
}
