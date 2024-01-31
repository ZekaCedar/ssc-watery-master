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

import guru.sfg.watery.domain.WaterOrder;
import guru.sfg.watery.domain.Customer;
import guru.sfg.watery.domain.OrderStatusEnum;
import guru.sfg.watery.repositories.WaterOrderRepository;
import guru.sfg.watery.repositories.CustomerRepository;
import guru.sfg.watery.web.mappers.WaterOrderMapper;
import guru.sfg.watery.web.model.WaterOrderDto;
import guru.sfg.watery.web.model.WaterOrderPagedList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class WaterOrderServiceImpl implements WaterOrderService {

    private final WaterOrderRepository waterOrderRepository;
    private final CustomerRepository customerRepository;
    private final WaterOrderMapper waterOrderMapper;

    @Override
    public WaterOrderPagedList listOrders(UUID customerId, Pageable pageable) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);

        if (customerOptional.isPresent()) {
            Page<WaterOrder> waterOrderPage =
                    waterOrderRepository.findAllByCustomer(customerOptional.get(), pageable);

            return new WaterOrderPagedList(waterOrderPage
                    .stream()
                    .map(waterOrderMapper::waterOrderToDto)
                    .collect(Collectors.toList()), PageRequest.of(
                    waterOrderPage.getPageable().getPageNumber(),
                    waterOrderPage.getPageable().getPageSize()),
                    waterOrderPage.getTotalElements());
        } else {
            return null;
        }
    }

    @Transactional
    @Override
    public WaterOrderDto placeOrder(UUID customerId, WaterOrderDto waterOrderDto) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);

        if (customerOptional.isPresent()) {
            WaterOrder waterOrder = waterOrderMapper.dtoToWaterOrder(waterOrderDto);
            waterOrder.setId(null); //should not be set by outside client
            waterOrder.setCustomer(customerOptional.get());
            waterOrder.setOrderStatus(OrderStatusEnum.NEW);

            waterOrder.getWaterOrderLines().forEach(line -> line.setWaterOrder(waterOrder));

            WaterOrder savedWaterOrder = waterOrderRepository.saveAndFlush(waterOrder);

            log.debug("Saved Water Order: " + waterOrder.getId());

            return waterOrderMapper.waterOrderToDto(savedWaterOrder);
        }
        //todo add exception type
        throw new RuntimeException("Customer Not Found");
    }

    @Override
    public WaterOrderDto getOrderById(UUID customerId, UUID orderId) {
        return waterOrderMapper.waterOrderToDto(getOrder(customerId, orderId));
    }

    @Override
    public void pickupOrder(UUID customerId, UUID orderId) {
        WaterOrder waterOrder = getOrder(customerId, orderId);
        waterOrder.setOrderStatus(OrderStatusEnum.PICKED_UP);

        waterOrderRepository.save(waterOrder);
    }

    private WaterOrder getOrder(UUID customerId, UUID orderId){
        Optional<Customer> customerOptional = customerRepository.findById(customerId);

        if(customerOptional.isPresent()){
            Optional<WaterOrder> waterOrderOptional = waterOrderRepository.findById(orderId);

            if(waterOrderOptional.isPresent()){
                WaterOrder waterOrder = waterOrderOptional.get();

                // fall to exception if customer id's do not match - order not for customer
                if(waterOrder.getCustomer().getId().equals(customerId)){
                    return waterOrder;
                }
            }
            throw new RuntimeException("Water Order Not Found");
        }
        throw new RuntimeException("Customer Not Found");
    }
}
