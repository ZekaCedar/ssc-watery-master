package guru.sfg.watery.web.controllers.api;

import guru.sfg.watery.web.controllers.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest
@SpringBootTest
public class WaterRestControllerIT extends BaseIT {

    @Test
    void deleteWaterHttpBasic() throws Exception{
        mockMvc.perform(delete("/api/v1/water/97df0c39-90c4-4ae0-b663-453e8e19c311")
                        .with(httpBasic("spring", "guru")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void deleteWaterHttpBasicUserRole() throws Exception{
        mockMvc.perform(delete("/api/v1/water/97df0c39-90c4-4ae0-b663-453e8e19c311")
                        .with(httpBasic("user", "password")))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteWaterHttpBasicCustomerRole() throws Exception{
        mockMvc.perform(delete("/api/v1/water/97df0c39-90c4-4ae0-b663-453e8e19c311")
                        .with(httpBasic("scott", "tiger")))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteWaterNoAuth() throws Exception{
        mockMvc.perform(delete("/api/v1/water/97df0c39-90c4-4ae0-b663-453e8e19c311"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteWaterUrl() throws Exception {
        mockMvc.perform(delete("/api/v1/water/97df0c39-90c4-4ae0-b663-453e8e19c311")
                        .param("apiKey","spring")
                        .param("apiSecret","guru"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteWaterBadCredsUrl() throws Exception {
        mockMvc.perform(delete("/api/v1/water/97df0c39-90c4-4ae0-b663-453e8e19c311")
                        .param("apiKey","spring")
                        .param("apiSecret","guruXXXX"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteWaterBadCreds() throws Exception {
        mockMvc.perform(delete("/api/v1/water/97df0c39-90c4-4ae0-b663-453e8e19c311")
                        .header("Api-Key","spring").header("Api-Secret","guruXXXX"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteWater() throws Exception {
        mockMvc.perform(delete("/api/v1/water/97df0c39-90c4-4ae0-b663-453e8e19c311")
                .header("Api-Key","spring").header("Api-Secret","guru"))
                .andExpect(status().isOk());
    }

    @Test
    void findWaters() throws Exception{
        mockMvc.perform(get("/api/v1/water/"))
                .andExpect(status().isOk());
    }

    @Test
    void findWaterById() throws Exception{
        mockMvc.perform(get("/api/v1/water/97df0c39-90c4-4ae0-b663-453e8e19c311"))
                .andExpect(status().isOk());
    }

    @Test
    void findWaterByUpc() throws Exception{
        mockMvc.perform(get("/api/v1/waterUpc/0631234200036"))
                .andExpect(status().isOk());
    }

}
