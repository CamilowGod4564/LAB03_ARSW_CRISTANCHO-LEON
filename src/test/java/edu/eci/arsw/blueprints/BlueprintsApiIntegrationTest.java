package edu.eci.arsw.blueprints;

import edu.eci.arsw.blueprints.filters.IdentityFilter;
import edu.eci.arsw.blueprints.filters.BlueprintsFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BlueprintsApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BlueprintsFilter filter;

    @Test
    void defaultProfileUsesIdentityFilter() {
        assertInstanceOf(IdentityFilter.class, filter);
    }

    @Test
    void createsAndRetrievesBlueprintWithApiResponse() throws Exception {
        String request = """
                {"author":"alice","name":"kitchen","points":[{"x":1,"y":1},{"x":2,"y":2}]}
                """;

        mockMvc.perform(post("/api/v1/blueprints")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.data.author").value("alice"));

        mockMvc.perform(get("/api/v1/blueprints/alice/kitchen"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.points.length()").value(2));
    }

    @Test
    void returnsUniformBadRequestForInvalidBlueprint() throws Exception {
        mockMvc.perform(post("/api/v1/blueprints")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"author\":\"\",\"name\":\"kitchen\",\"points\":[]}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    void returnsNotFoundWhenAddingPointToUnknownBlueprint() throws Exception {
        mockMvc.perform(put("/api/v1/blueprints/nobody/missing/points")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"x\":3,\"y\":4}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    void acceptsNewPointsAndRejectsDuplicateBlueprints() throws Exception {
        String request = """
                {"author":"bob","name":"garage","points":[{"x":1,"y":1}]}
                """;

        mockMvc.perform(post("/api/v1/blueprints")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated());

        mockMvc.perform(put("/api/v1/blueprints/bob/garage/points")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"x\":3,\"y\":4}"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.code").value(202));

        mockMvc.perform(post("/api/v1/blueprints")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(409));
    }
}
