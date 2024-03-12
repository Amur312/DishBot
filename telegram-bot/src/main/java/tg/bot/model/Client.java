package tg.bot.model;

import jakarta.persistence.*;
import lombok.Data;
import tg.bot.model.enums.BotState;

@Entity
@Table(name = "clients")
@Data
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "chat_id", nullable = false, unique = true)
    private Long chatId;

    @Column(name = "tg_name")
    private String tgName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private BotState state = BotState.NONE;
    @Column(name = "address_client")
    private String address;
    public Client() {
    }

    public Client(String lastName, String firstName, long chatId, String phoneNumber) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.chatId = chatId;
        this.phoneNumber = phoneNumber;
        this.state = BotState.NONE;
    }

    public Client(long chatId, String tgName, String phoneNumber) {
        this.chatId = chatId;
        this.tgName = tgName;
        this.phoneNumber = phoneNumber;
        this.state = BotState.NONE;
    }
}
