package tg.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tg.bot.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
