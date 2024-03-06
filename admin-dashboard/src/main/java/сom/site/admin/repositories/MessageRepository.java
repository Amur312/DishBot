package сom.site.admin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import сom.site.admin.models.entities.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

}
