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

import guru.sfg.watery.domain.WaterOrderLine;
import guru.sfg.watery.repositories.WaterRepository;
import guru.sfg.watery.web.model.WaterOrderLineDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class WaterOrderLineMapperDecorator implements WaterOrderLineMapper {
    private WaterRepository waterRepository;
    private WaterOrderLineMapper waterOrderLineMapper;

    /**
     * @param waterRepository
     */
    @Autowired
    public void setWaterRepository(WaterRepository waterRepository) {
        this.waterRepository = waterRepository;
    }

    /**
     * @param waterOrderLineMapper
     */
    @Autowired
    @Qualifier("delegate")
    public void setWaterOrderLineMapper(WaterOrderLineMapper waterOrderLineMapper) {
        this.waterOrderLineMapper = waterOrderLineMapper;
    }

    @Override
    public WaterOrderLineDto waterOrderLineToDto(WaterOrderLine line) {
        WaterOrderLineDto orderLineDto = waterOrderLineMapper.waterOrderLineToDto(line);
        orderLineDto.setWaterId(line.getWater().getId());
        return orderLineDto;
    }

    @Override
    public WaterOrderLine dtoToWaterOrderLine(WaterOrderLineDto dto) {
        WaterOrderLine waterOrderLine = waterOrderLineMapper.dtoToWaterOrderLine(dto);
        waterOrderLine.setWater(waterRepository.getOne(dto.getWaterId()));
        waterOrderLine.setQuantityAllocated(0);
        return waterOrderLine;
    }
}
