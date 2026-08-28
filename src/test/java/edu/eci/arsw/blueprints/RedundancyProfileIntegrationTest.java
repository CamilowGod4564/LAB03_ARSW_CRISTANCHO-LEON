package edu.eci.arsw.blueprints;

import edu.eci.arsw.blueprints.filters.BlueprintsFilter;
import edu.eci.arsw.blueprints.filters.RedundancyFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:redundancydb;DB_CLOSE_DELAY=-1")
@ActiveProfiles({"test", "redundancy"})
class RedundancyProfileIntegrationTest {

    @Autowired
    private BlueprintsFilter filter;

    @Test
    void redundancyProfileSelectsRedundancyFilter() {
        assertInstanceOf(RedundancyFilter.class, filter);
    }
}
