package com.site.admin.services.impl;

import java.util.List;

import com.site.admin.exceptions.ValidationException;
import com.site.admin.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.site.admin.models.entities.Order;
import com.site.admin.services.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;

    @Autowired
    public OrderServiceImpl(OrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public Order findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id of Order should not be NULL");
        }

        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Order> findAll() {
        return repository.findAll();
    }

    @Override
    public Order save(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order should not be NULL");
        }
        if (order.getId() != null) {
            throw new ValidationException("Id of Order should be NULL");
        }

        return repository.save(order);
    }

    @Override
    public Order update(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order should not be NULL");
        }
        if (order.getId() == null) {
            throw new ValidationException("Id of Order should not be NULL");
        }

        return repository.save(order);
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id of Order should not be NULL");
        }

        repository.deleteById(id);
    }

}
