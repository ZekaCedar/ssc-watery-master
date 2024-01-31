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

package guru.sfg.watery.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WaterDto extends BaseItem {

    @Builder
    public WaterDto(UUID id, Integer version, OffsetDateTime createdDate, OffsetDateTime lastModifiedDate, String waterName,
                    WaterStyleEnum waterStyle, String upc, Integer quantityOnHand, BigDecimal price) {
        super(id, version, createdDate, lastModifiedDate);
        this.waterName = waterName;
        this.waterStyle = waterStyle;
        this.upc = upc;
        this.quantityOnHand = quantityOnHand;
        this.price = price;
    }

    private String waterName;
    private WaterStyleEnum waterStyle;
    private String upc;
    private Integer quantityOnHand;

    @JsonFormat(shape= JsonFormat.Shape.STRING)
    private BigDecimal price;

}
