package сom.site.admin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import сom.site.admin.models.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

}
