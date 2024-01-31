/*
 *  Copyright 2020 the original author or authors.
 *
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package guru.sfg.watery.web.controllers;

import guru.sfg.watery.domain.Water;
import guru.sfg.watery.repositories.WaterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class WaterControllerTest {
    @Mock
    WaterRepository waterRepository;

    @InjectMocks
    WaterController controller;
    List<Water> waterList;
    UUID uuid;
    Water water;

    MockMvc mockMvc;
    Page<Water> waters;
    Page<Water> pagedResponse;

    @BeforeEach
    void setUp() {
        waterList = new ArrayList<Water>();
        waterList.add(Water.builder().build());
        waterList.add(Water.builder().build());
        pagedResponse = new PageImpl(waterList);

        final String id = "493410b3-dd0b-4b78-97bf-289f50f6e74f";
        uuid = UUID.fromString(id);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void findWaters() throws Exception{
        mockMvc.perform(get("/waters/find"))
                .andExpect(status().isOk())
                .andExpect(view().name("waters/findWaters"))
                .andExpect(model().attributeExists("water"));
        verifyZeroInteractions(waterRepository);
    }

    //ToDO: Mocking Page
     void processFindFormReturnMany() throws Exception{
        when(waterRepository.findAllByWaterName(anyString(), PageRequest.of(0,
              10,Sort.Direction.DESC,"waterName"))).thenReturn(pagedResponse);
        mockMvc.perform(get("/waters"))
                .andExpect(status().isOk())
                .andExpect(view().name("waters/waterList"))
                .andExpect(model().attribute("selections", hasSize(2)));
    }


    @Test
    void showWater() throws Exception{

        when(waterRepository.findById(uuid)).thenReturn(Optional.of(Water.builder().id(uuid).build()));
        mockMvc.perform(get("/waters/"+uuid))
                .andExpect(status().isOk())
                .andExpect(view().name("waters/waterDetails"))
                .andExpect(model().attribute("water", hasProperty("id", is(uuid))));
    }

    @Test
    void initCreationForm() throws Exception {
        mockMvc.perform(get("/waters/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("waters/createWater"))
                .andExpect(model().attributeExists("water"));
        verifyZeroInteractions(waterRepository);
    }

    @Test
    void processCreationForm() throws Exception {
        when(waterRepository.save(ArgumentMatchers.any())).thenReturn(Water.builder().id(uuid).build());
        mockMvc.perform(post("/waters/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/waters/"+ uuid))
                .andExpect(model().attributeExists("water"));
        verify(waterRepository).save(ArgumentMatchers.any());
    }

    @Test
    void initUpdateWaterForm() throws Exception{
        when(waterRepository.findById(uuid)).thenReturn(Optional.of(Water.builder().id(uuid).build()));
        mockMvc.perform(get("/waters/"+uuid+"/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("waters/createOrUpdateWater"))
                .andExpect(model().attributeExists("water"));
        verifyZeroInteractions(waterRepository);
    }

    @Test
    void processUpdationForm() throws Exception {
        when(waterRepository.save(ArgumentMatchers.any())).thenReturn(Water.builder().id(uuid).build());

        mockMvc.perform(post("/waters/"+uuid+"/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/waters/"+uuid))
                .andExpect(model().attributeExists("water"));

        verify(waterRepository).save(ArgumentMatchers.any());
    }
}