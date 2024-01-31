package guru.sfg.watery.web.controllers;

import guru.sfg.watery.repositories.CustomerRepository;
import guru.sfg.watery.repositories.WaterInventoryRepository;
import guru.sfg.watery.repositories.WaterRepository;
import guru.sfg.watery.services.WaterService;
import guru.sfg.watery.services.WateryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by jt on 6/12/20.
 */
@WebMvcTest
public class WaterControllerIT {

    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;

    @MockBean
    WaterRepository waterRepository;

    @MockBean
    WaterInventoryRepository waterInventoryRepository;

    @MockBean
    WateryService wateryService;

    @MockBean
    CustomerRepository customerRepository;

    @MockBean
    WaterService waterService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser("spring")
    // also pass @WithMockUser("springanything")
    @Test
    void findWaters() throws Exception{
        mockMvc.perform(get("/waters/find"))
                .andExpect(status().isOk())
                .andExpect(view().name("waters/findWaters"))
                .andExpect(model().attributeExists("water"));
    }

    @Test
    void findWatersWithHttpBasic() throws Exception{
        mockMvc.perform(get("/waters/find").with(httpBasic("spring","guru")))
                .andExpect(status().isOk())
                .andExpect(view().name("waters/findWaters"))
                .andExpect(model().attributeExists("water"));
    }


}