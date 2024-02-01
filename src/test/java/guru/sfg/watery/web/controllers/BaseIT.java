package guru.sfg.watery.web.controllers;

import guru.sfg.watery.repositories.CustomerRepository;
import guru.sfg.watery.repositories.WaterInventoryRepository;
import guru.sfg.watery.repositories.WaterRepository;
import guru.sfg.watery.services.WaterService;
import guru.sfg.watery.services.WateryService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

/**
 * Created by jt on 6/13/20.
 */
public abstract class BaseIT {

    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;

    @MockBean
    WaterRepository waterRepository;

    @MockBean
    WaterInventoryRepository waterInventoryRepository;

    @MockBean
    WateryService breweryService;

    @MockBean
    CustomerRepository customerRepository;

    @MockBean
    WaterService waterService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

}
