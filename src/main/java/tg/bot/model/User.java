package tg.bot.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "chat_id", nullable = false, unique = true)
    private long chatId;

    public User(String userName,long chatId) {
        this.userName = userName;
        this.chatId = chatId;

    }

    public User() {

    }
}
