package guru.sfg.watery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class WateryControllerIT extends BaseIT{

    @Test
    void listWateriesCustomerRole() throws Exception{
        mockMvc.perform(get("/watery/wateries")
                        .with(httpBasic("scott", "tiger")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void listWateriesAdminRole() throws Exception{
        mockMvc.perform(get("/watery/wateries")
                        .with(httpBasic("spring", "guru")))
                .andExpect(status().isForbidden());
    }

    @Test
    void listWateriesUserRole() throws Exception{
        mockMvc.perform(get("/watery/wateries")
                        .with(httpBasic("user", "password")))
                .andExpect(status().isForbidden());
    }

    @Test
    void listWateriesNoAuth() throws Exception{
        mockMvc.perform(delete("/watery/wateries"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getWateriesJsonCustomerRole() throws Exception{
        mockMvc.perform(get("/watery/api/v1/wateries")
                        .with(httpBasic("scott", "tiger")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getWateriesJsonAdminRole() throws Exception{
        mockMvc.perform(get("/watery/api/v1/wateries")
                        .with(httpBasic("spring", "guru")))
                .andExpect(status().isForbidden());
    }

    @Test
    void getWateriesJsonUserRole() throws Exception{
        mockMvc.perform(get("/watery/api/v1/wateries")
                        .with(httpBasic("user", "password")))
                .andExpect(status().isForbidden());
    }

    @Test
    void getWateriesJsonNoAuth() throws Exception{
        mockMvc.perform(delete("/watery/api/v1/wateries"))
                .andExpect(status().isUnauthorized());
    }
}
