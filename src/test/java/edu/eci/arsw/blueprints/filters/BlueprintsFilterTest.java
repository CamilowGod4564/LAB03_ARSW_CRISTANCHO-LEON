package edu.eci.arsw.blueprints.filters;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BlueprintsFilterTest {

    @Test
    void redundancyFilterRemovesOnlyConsecutiveDuplicatePoints() {
        Blueprint blueprint = new Blueprint("author", "name", List.of(
                new Point(0, 0), new Point(0, 0), new Point(1, 1), new Point(0, 0)));

        Blueprint filtered = new RedundancyFilter().apply(blueprint);

        assertEquals(List.of(new Point(0, 0), new Point(1, 1), new Point(0, 0)), filtered.getPoints());
    }

    @Test
    void undersamplingFilterKeepsPointsAtEvenIndexes() {
        Blueprint blueprint = new Blueprint("author", "name", List.of(
                new Point(0, 0), new Point(1, 1), new Point(2, 2), new Point(3, 3), new Point(4, 4)));

        Blueprint filtered = new UndersamplingFilter().apply(blueprint);

        assertEquals(List.of(new Point(0, 0), new Point(2, 2), new Point(4, 4)), filtered.getPoints());
    }
}
