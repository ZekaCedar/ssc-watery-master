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

package guru.sfg.watery.web.mappers;

import guru.sfg.watery.domain.WaterOrder;
import guru.sfg.watery.web.model.WaterOrderDto;
import org.mapstruct.Mapper;

@Mapper(uses = {DateMapper.class, WaterOrderLineMapper.class})
public interface WaterOrderMapper {

    WaterOrderDto waterOrderToDto(WaterOrder waterOrder);

    WaterOrder dtoToWaterOrder(WaterOrderDto dto);
}
