package tg.bot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cart_item")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany
    private List<Product> product;
    private Integer quantity;
    private Long chatId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CartItem cartItem = (CartItem) o;
        return Objects.equals(id, cartItem.id) &&
                Objects.equals(product, cartItem.product) &&
                Objects.equals(quantity, cartItem.quantity);

    }

    @Override
    public int hashCode() {
        return Objects.hash(id, product, quantity);
    }

    @Override
    public String toString() {
        return "CartItem [id=" + id + ", product=" + product + ", quantity=" + quantity + "]";
    }
}
