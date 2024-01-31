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

import guru.sfg.watery.domain.Water;
import guru.sfg.watery.repositories.WaterRepository;
import guru.sfg.watery.web.mappers.WaterMapper;
import guru.sfg.watery.web.model.WaterDto;
import guru.sfg.watery.web.model.WaterPagedList;
import guru.sfg.watery.web.model.WaterStyleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class WaterServiceImpl implements WaterService {

    private final WaterRepository waterRepository;
    private final WaterMapper waterMapper;

    @Override
    public WaterPagedList listWaters(String waterName, WaterStyleEnum waterStyle, PageRequest pageRequest, Boolean showInventoryOnHand) {

        log.debug("Listing Waters");

        WaterPagedList waterPagedList;
        Page<Water> waterPage;

        if (!StringUtils.isEmpty(waterName) && !StringUtils.isEmpty(waterStyle)) {
            //search both
            waterPage = waterRepository.findAllByWaterNameAndWaterStyle(waterName, waterStyle, pageRequest);
        } else if (!StringUtils.isEmpty(waterName) && StringUtils.isEmpty(waterStyle)) {
            //search water_service name
            waterPage = waterRepository.findAllByWaterName(waterName, pageRequest);
        } else if (StringUtils.isEmpty(waterName) && !StringUtils.isEmpty(waterStyle)) {
            //search water_service style
            waterPage = waterRepository.findAllByWaterStyle(waterStyle, pageRequest);
        } else {
            waterPage = waterRepository.findAll(pageRequest);
        }

        if (showInventoryOnHand) {
            waterPagedList = new WaterPagedList(waterPage
                    .getContent()
                    .stream()
                    .map(waterMapper::waterToWaterDto)
                    .collect(Collectors.toList()),
                    PageRequest
                            .of(waterPage.getPageable().getPageNumber(),
                                    waterPage.getPageable().getPageSize()),
                    waterPage.getTotalElements());

        } else {
            waterPagedList = new WaterPagedList(waterPage
                    .getContent()
                    .stream()
                    .map(waterMapper::waterToWaterDto)
                    .collect(Collectors.toList()),
                    PageRequest
                            .of(waterPage.getPageable().getPageNumber(),
                                    waterPage.getPageable().getPageSize()),
                    waterPage.getTotalElements());
        }
        return waterPagedList;
    }

    @Override
    public WaterDto findWaterById(UUID waterId, Boolean showInventoryOnHand) {

        log.debug("Finding Water by id: " + waterId);

        Optional<Water> waterOptional = waterRepository.findById(waterId);

        if (waterOptional.isPresent()) {
            log.debug("Found WaterId: " + waterId);
            if(showInventoryOnHand) {
                return waterMapper.waterToWaterDto(waterOptional.get());
            } else {
                return waterMapper.waterToWaterDto(waterOptional.get());
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found. UUID: " + waterId);
        }
    }

    @Override
    public WaterDto saveWater(WaterDto waterDto) {
        return waterMapper.waterToWaterDto(waterRepository.save(waterMapper.waterDtoToWater(waterDto)));
    }

    @Override
    public void updateWater(UUID waterId, WaterDto waterDto) {
        Optional<Water> waterOptional = waterRepository.findById(waterId);

        waterOptional.ifPresentOrElse(water -> {
            water.setWaterName(waterDto.getWaterName());
            water.setWaterStyle(waterDto.getWaterStyle());
            water.setPrice(waterDto.getPrice());
            water.setUpc(waterDto.getUpc());
            waterRepository.save(water);
        }, () -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found. UUID: " + waterId);
        });
    }

    @Override
    public void deleteById(UUID waterId) {
        waterRepository.deleteById(waterId);
    }

    @Override
    public WaterDto findWaterByUpc(String upc) {
        return waterMapper.waterToWaterDto(waterRepository.findByUpc(upc));
    }
}
