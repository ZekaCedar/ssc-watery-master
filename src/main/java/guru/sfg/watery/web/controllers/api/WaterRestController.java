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

package guru.sfg.watery.web.controllers.api;

import guru.sfg.watery.services.WaterService;
import guru.sfg.watery.web.model.WaterDto;
import guru.sfg.watery.web.model.WaterPagedList;
import guru.sfg.watery.web.model.WaterStyleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
@RestController
public class WaterRestController {

    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 25;

    private final WaterService waterService;

    @GetMapping(produces = { "application/json" }, path = "water")
    public ResponseEntity<WaterPagedList> listWaters(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                    @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                    @RequestParam(value = "waterName", required = false) String waterName,
                                                    @RequestParam(value = "waterStyle", required = false) WaterStyleEnum waterStyle,
                                                    @RequestParam(value = "showInventoryOnHand", required = false) Boolean showInventoryOnHand){

        log.debug("Listing Waters");

        if (showInventoryOnHand == null) {
            showInventoryOnHand = false;
        }

        if (pageNumber == null || pageNumber < 0){
            pageNumber = DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        WaterPagedList waterList = waterService.listWaters(waterName, waterStyle, PageRequest.of(pageNumber, pageSize), showInventoryOnHand);

        return new ResponseEntity<>(waterList, HttpStatus.OK);
    }

    @GetMapping(path = {"water/{waterId}"}, produces = { "application/json" })
    public ResponseEntity<WaterDto> getWaterById(@PathVariable("waterId") UUID waterId,
                                                @RequestParam(value = "showInventoryOnHand", required = false) Boolean showInventoryOnHand){

        log.debug("Get Request for WaterId: " + waterId);

        if (showInventoryOnHand == null) {
            showInventoryOnHand = false;
        }

        return new ResponseEntity<>(waterService.findWaterById(waterId, showInventoryOnHand), HttpStatus.OK);
    }

    @GetMapping(path = {"waterUpc/{upc}"}, produces = { "application/json" })
    public ResponseEntity<WaterDto> getWaterByUpc(@PathVariable("upc") String upc){
        return new ResponseEntity<>(waterService.findWaterByUpc(upc), HttpStatus.OK);
    }

    @PostMapping(path = "water")
    public ResponseEntity saveNewWater(@Valid @RequestBody WaterDto waterDto){

        WaterDto savedDto = waterService.saveWater(waterDto);

        HttpHeaders httpHeaders = new HttpHeaders();

        //todo hostname for uri
        httpHeaders.add("Location", "/api/v1/water_service/" + savedDto.getId().toString());

        return new ResponseEntity(httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping(path = {"water/{waterId}"}, produces = { "application/json" })
    public ResponseEntity updateWater(@PathVariable("waterId") UUID waterId, @Valid @RequestBody WaterDto waterDto){

        waterService.updateWater(waterId, waterDto);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping({"water/{waterId}"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWater(@PathVariable("waterId") UUID waterId){
        waterService.deleteById(waterId);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<List> badReqeustHandler(ConstraintViolationException e){
        List<String> errors = new ArrayList<>(e.getConstraintViolations().size());

        e.getConstraintViolations().forEach(constraintViolation -> {
            errors.add(constraintViolation.getPropertyPath().toString() + " : " + constraintViolation.getMessage());
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
