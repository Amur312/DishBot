package com.site.admin.services;

import java.util.List;

import com.site.admin.models.entities.Message;

public interface MessageService {

    Message findById(Integer id);

    Message update(Message message);

    List<Message> findAll();

}
