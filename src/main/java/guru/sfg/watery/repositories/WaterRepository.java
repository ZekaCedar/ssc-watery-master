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
package guru.sfg.watery.repositories;

import guru.sfg.watery.domain.Water;
import guru.sfg.watery.web.model.WaterStyleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Created by jt on 2019-01-26.
 */
public interface WaterRepository extends JpaRepository<Water, UUID> {
    
    Page<Water> findAllByWaterName(String waterName, Pageable pageable);

    Page<Water> findAllByWaterNameIsLike(String waterName, Pageable pageable);

    Page<Water> findAllByWaterStyle(WaterStyleEnum waterStyle, Pageable pageable);

    Page<Water> findAllByWaterNameAndWaterStyle(String waterName, WaterStyleEnum waterStyle, Pageable pageable);

    Water findByUpc(String upc);
}
