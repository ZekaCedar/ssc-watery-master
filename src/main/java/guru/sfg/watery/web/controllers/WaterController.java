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
import guru.sfg.watery.repositories.WaterInventoryRepository;
import guru.sfg.watery.repositories.WaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@RequestMapping("/waters")
@Controller
public class WaterController {

    private final WaterRepository waterRepository;
    private final WaterInventoryRepository waterInventoryRepository;


    @RequestMapping("/find")
    public String findWaters(Model model) {
        model.addAttribute("water", Water.builder().build());
        return "waters/findWaters";
    }

    @GetMapping
    public String processFindFormReturnMany(Water water, BindingResult result, Model model) {
        // find waters by name
        //ToDO: Add Service
        //ToDO: Get paging data from view
        Page<Water> pagedResult = waterRepository.findAllByWaterNameIsLike("%" + water.getWaterName() + "%", createPageRequest(0, 10, Sort.Direction.DESC, "waterName"));
        List<Water> waterList = pagedResult.getContent();
        if (waterList.isEmpty()) {
            // no waters found
            result.rejectValue("waterName", "notFound", "not found");
            return "waters/findWaters";
        } else if (waterList.size() == 1) {
            // 1 water found
            water = waterList.get(0);
            return "redirect:/waters/" + water.getId();
        } else {
            // multiple waters found
            model.addAttribute("selections", waterList);
            return "waters/waterList";
        }
    }


    @GetMapping("/{waterId}")
    public ModelAndView showWater(@PathVariable UUID waterId) {
        ModelAndView mav = new ModelAndView("waters/waterDetails");
        //ToDO: Add Service
        mav.addObject(waterRepository.findById(waterId).get());
        return mav;
    }

    @GetMapping("/new")
    public String initCreationForm(Model model) {
        model.addAttribute("water", Water.builder().build());
        return "waters/createWater";
    }

    @PostMapping("/new")
    public String processCreationForm(Water water) {
        //ToDO: Add Service
        Water newWater = Water.builder()
                .waterName(water.getWaterName())
                .waterStyle(water.getWaterStyle())
                .minOnHand(water.getMinOnHand())
                .price(water.getPrice())
                .quantityToBrew(water.getQuantityToBrew())
                .upc(water.getUpc())
                .build();

        Water savedWater = waterRepository.save(newWater);
        return "redirect:/waters/" + savedWater.getId();
    }

    @GetMapping("/{waterId}/edit")
    public String initUpdateWaterForm(@PathVariable UUID waterId, Model model) {
        if (waterRepository.findById(waterId).isPresent())
            model.addAttribute("water", waterRepository.findById(waterId).get());
        return "waters/createOrUpdateWater";
    }

    @PostMapping("/{waterId}/edit")
    public String processUpdateForm(@Valid Water water, BindingResult result) {
        if (result.hasErrors()) {
            return "waters/createOrUpdateWater";
        } else {
            //ToDO: Add Service
            Water savedWater = waterRepository.save(water);
            return "redirect:/waters/" + savedWater.getId();
        }
    }

    private PageRequest createPageRequest(int page, int size, Sort.Direction sortDirection, String propertyName) {
        return PageRequest.of(page,
                size,
                Sort.by(sortDirection, propertyName));
    }
}


