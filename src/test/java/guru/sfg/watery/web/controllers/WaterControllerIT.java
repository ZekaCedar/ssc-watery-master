package guru.sfg.watery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by jt on 6/12/20.
 */
@WebMvcTest
public class WaterControllerIT extends BaseIT{

//    @WithMockUser("spring")
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

    @Test
    void findWatersWithAnonymous() throws Exception{
//        mockMvc.perform(get("/waters/find").with(httpBasic("spring","guru")))
        mockMvc.perform(get("/waters/find").with(anonymous()))
                .andExpect(status().isOk())
                .andExpect(view().name("waters/findWaters"))
                .andExpect(model().attributeExists("water"));
    }


}