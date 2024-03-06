package сom.site.admin.services;

import java.util.List;

import сom.site.admin.models.entities.Message;

public interface MessageService {

    Message findById(Integer id);

    Message update(Message message);

    List<Message> findAll();

}
