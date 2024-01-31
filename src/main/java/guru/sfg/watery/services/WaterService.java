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

package guru.sfg.watery.services;

import guru.sfg.watery.web.model.WaterDto;
import guru.sfg.watery.web.model.WaterPagedList;
import guru.sfg.watery.web.model.WaterStyleEnum;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public interface WaterService {

    WaterPagedList listWaters(String waterName, WaterStyleEnum waterStyle, PageRequest pageRequest, Boolean showInventoryOnHand);

    WaterDto findWaterById(UUID waterId, Boolean showInventoryOnHand);

    WaterDto saveWater(WaterDto waterDto);

    void updateWater(UUID waterId, WaterDto waterDto);

    void deleteById(UUID waterId);

    WaterDto findWaterByUpc(String upc);
}
