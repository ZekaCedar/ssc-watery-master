package guru.sfg.watery.web.controllers.api;

import guru.sfg.watery.web.controllers.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class WaterRestControllerIT extends BaseIT {

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
