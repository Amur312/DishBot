package com.site.admin.services.impl;

import java.util.List;
import java.util.Optional;

import com.site.admin.exceptions.ValidationException;
import com.site.admin.models.entities.Order;
import com.site.admin.repositories.OrderRepository;
import com.site.admin.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;

    @Autowired
    public OrderServiceImpl(OrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Order> findById(Long id) {
        Assert.notNull(id, "Идентификатор заказа не должен быть пустым");
        return repository.findById(id);
    }

    @Override
    public List<Order> findAll() {
        return repository.findAll();
    }

    @Transactional
    @Override
    public Order save(Order order) {
        Assert.notNull(order, "Заказ не должен быть пустым");
        Assert.isNull(order.getId(), "Идентификатор заказа должен быть пустым при создании нового заказа");

        return repository.save(order);
    }

    @Transactional
    @Override
    public Order update(Order order) {
        Assert.notNull(order, "Заказ не должен быть пустым");
        Assert.notNull(order.getId(), "Идентификатор заказа не должен быть пустым при обновлении");

        repository.findById(order.getId())
                .orElseThrow(() -> new ValidationException("Заказ с данным идентификатором не существует, обновление невозможно"));

        return repository.save(order);
    }

    @Override
    public void deleteById(Long id) {
        Assert.notNull(id, "Идентификатор заказа не должен быть пустым");

        repository.deleteById(id);
    }

}
