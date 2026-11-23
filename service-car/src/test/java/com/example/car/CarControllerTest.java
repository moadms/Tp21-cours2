package com.example.car;

import com.example.car.entities.Client;
import com.example.car.services.ClientApi;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "eureka.client.enabled=false")
@AutoConfigureMockMvc
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientApi clientApi;

    @Test
    void shouldSaveAndReturnEnrichedCarViaWebClient() throws Exception {
        Mockito.when(clientApi.findClientById(1L)).thenReturn(new Client(1L, "Salma", 22.0f));

        mockMvc.perform(post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"marque\":\"Toyota\",\"modele\":\"Yaris\",\"clientId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.marque").value("Toyota"));

        mockMvc.perform(get("/api/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].client.nom").value("Salma"));
    }
}
