package com.site.admin.services.impl;

import java.util.List;

import com.site.admin.exceptions.ValidationException;
import com.site.admin.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.site.admin.models.entities.Client;
import com.site.admin.services.ClientService;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;

    @Autowired
    public ClientServiceImpl(ClientRepository repository) {
        this.repository = repository;
    }

    @Override
    public Client findById(Long id) {
        Assert.notNull(id, "Идентификатор клиента не должен быть пустым.");
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Client> findAll() {
        return repository.findAll();
    }

    @Override
    public Client update(Client client) {
        Assert.notNull(client, "Клиент не должен быть пустым.");
        Assert.notNull(client.getId(), "Идентификатор клиента не должен быть пустым.");
        Assert.notNull(client.getChatId(), "Идентификатор чата клиента не должен быть пустым.");

        return repository.save(client);
    }
}
