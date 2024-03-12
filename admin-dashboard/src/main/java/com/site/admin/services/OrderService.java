package com.site.admin.services;

import java.util.List;

import com.site.admin.models.entities.Order;

public interface OrderService {

    Order findById(Long id);

    List<Order> findAll();

    Order save(Order order);

    Order update(Order order);

    void deleteById(Long id);

}
