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
package guru.sfg.watery.bootstrap;

import guru.sfg.watery.domain.*;
import guru.sfg.watery.repositories.*;
import guru.sfg.watery.web.model.WaterStyleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;


/**
 * Created by jt on 2019-01-26.
 */
@RequiredArgsConstructor
@Component
public class DefaultWateryLoader implements CommandLineRunner {

    public static final String TASTING_ROOM = "Tasting Room";
    public static final String WATER_1_UPC = "0631234200036";
    public static final String WATER_2_UPC = "0631234300019";
    public static final String WATER_3_UPC = "0083783375213";

    private final WateryRepository wateryRepository;
    private final WaterRepository waterRepository;
    private final WaterInventoryRepository waterInventoryRepository;
    private final WaterOrderRepository waterOrderRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) {
        loadWateryData();
        loadCustomerData();
    }

    private void loadCustomerData() {
        Customer tastingRoom = Customer.builder()
                .customerName(TASTING_ROOM)
                .apiKey(UUID.randomUUID())
                .build();

        customerRepository.save(tastingRoom);

        waterRepository.findAll().forEach(water -> {
            waterOrderRepository.save(WaterOrder.builder()
                    .customer(tastingRoom)
                    .orderStatus(OrderStatusEnum.NEW)
                    .waterOrderLines(Set.of(WaterOrderLine.builder()
                            .water(water)
                            .orderQuantity(2)
                            .build()))
                    .build());
        });
    }

    private void loadWateryData() {
        if (wateryRepository.count() == 0) {
            wateryRepository.save(Watery
                    .builder()
                    .wateryName("Cage Brewing")
                    .build());

            Water mangoBobs = Water.builder()
                    .waterName("Mango Bobs")
                    .waterStyle(WaterStyleEnum.IPA)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(WATER_1_UPC)
                    .build();

            waterRepository.save(mangoBobs);
            waterInventoryRepository.save(WaterInventory.builder()
                    .water(mangoBobs)
                    .quantityOnHand(500)
                    .build());

            Water galaxyCat = Water.builder()
                    .waterName("Galaxy Cat")
                    .waterStyle(WaterStyleEnum.PALE_ALE)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(WATER_2_UPC)
                    .build();

            waterRepository.save(galaxyCat);
            waterInventoryRepository.save(WaterInventory.builder()
                    .water(galaxyCat)
                    .quantityOnHand(500)
                    .build());

            Water pinball = Water.builder()
                    .waterName("Pinball Porter")
                    .waterStyle(WaterStyleEnum.PORTER)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(WATER_3_UPC)
                    .build();

            waterRepository.save(pinball);
            waterInventoryRepository.save(WaterInventory.builder()
                    .water(pinball)
                    .quantityOnHand(500)
                    .build());

        }
    }
}
