package сom.site.admin.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import сom.site.admin.models.entities.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    List<Client> findAllByActive(boolean active);

}
