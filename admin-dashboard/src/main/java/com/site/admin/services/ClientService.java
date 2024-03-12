package com.site.admin.services;

import java.util.List;

import com.site.admin.models.entities.Client;

public interface ClientService {

    Client findById(Long id);

    List<Client> findAll();

    Client update(Client client);



}
