package com.site.admin.services;

import java.util.List;
import java.util.Optional;

import com.site.admin.models.entities.Order;

public interface OrderService {

    Optional<Order> findById(Long id);

    List<Order> findAll();

    Order save(Order order);

    Order update(Order order);

    void deleteById(Long id);

}
