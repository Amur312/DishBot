package com.site.admin.services.impl;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.site.admin.models.entities.Client;
import com.site.admin.services.BroadcastService;
import com.site.admin.services.ClientService;
import com.site.admin.services.TelegramService;

@Service
public class BroadcastServiceImpl implements BroadcastService {

    private static final Logger LOG = LogManager.getLogger(BroadcastServiceImpl.class);

    private final TelegramService telegramService;
    private final ClientService clientService;

    private final ExecutorService broadcastQueue = Executors.newSingleThreadExecutor();

    @Autowired
    public BroadcastServiceImpl(TelegramService telegramService, ClientService clientService) {
        this.telegramService = telegramService;
        this.clientService = clientService;
    }

    @Override
    public void send(String message) {
        broadcastQueue.submit(() -> {
            sendMessageToClients(message, clientService.findAllByActive(true));
        });
    }

    private void sendMessageToClients(String message, List<Client> clients) {
        for (Client client : clients) {
            try {
                telegramService.sendMessage(client.getChatId(), message);
            } catch (Exception e) {
                if (e.toString().contains("Forbidden: bot was blocked by the user")) {
                    deactivateClient(client);
                } else {
                    LOG.error("Failed send message '{}' to client '{}'", message, client, e);
                }
            }
        }
    }

    private void deactivateClient(Client client) {
        client.setActive(false);
        clientService.update(client);
    }

}
