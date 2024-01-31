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

import guru.sfg.watery.domain.Watery;
import guru.sfg.watery.services.WateryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@RequiredArgsConstructor
@RequestMapping("/watery")
@Controller
public class WateryController {

    private final WateryService wateryService;

    @GetMapping({"/wateries", "/wateries/index", "/wateries/index.html", "/wateries.html"})
    public String listWateries(Model model) {
        model.addAttribute("wateries", wateryService.getAllWateries());
        return "wateries/index";
    }
    @GetMapping("/api/v1/wateries")
    public @ResponseBody
    List<Watery> getWateriesJson(){
        return wateryService.getAllWateries();
    }
}
