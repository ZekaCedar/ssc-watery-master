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
package guru.sfg.watery.domain;

import guru.sfg.watery.web.model.WaterStyleEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by jt on 2019-01-26.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Water extends BaseEntity {

    @Builder
    public Water(UUID id, Long version, Timestamp createdDate, Timestamp lastModifiedDate, String waterName,
                 WaterStyleEnum waterStyle, String upc, Integer minOnHand,
                 Integer quantityToBrew, BigDecimal price, Set<WaterInventory> waterInventory) {
        super(id, version, createdDate, lastModifiedDate);
        this.waterName = waterName;
        this.waterStyle = waterStyle;
        this.upc = upc;
        this.minOnHand = minOnHand;
        this.quantityToBrew = quantityToBrew;
        this.price = price;
        this.waterInventory = waterInventory;
    }

    private String waterName;
    private WaterStyleEnum waterStyle;

    @Column(unique = true)
    private String upc;

    /**
     * Min on hand qty - used to trigger brew
     */
    private Integer minOnHand;
    private Integer quantityToBrew;
    private BigDecimal price;

    @OneToMany(mappedBy = "water", cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    private Set<WaterInventory> waterInventory = new HashSet<>();
}
