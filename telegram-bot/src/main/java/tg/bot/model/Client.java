package tg.bot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tg.bot.model.enums.BotState;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Override
    public String toString() {
        return "Client [id=" + id + ", chatId=" + chatId + ", lastName=" + lastName +
                ", firstName=" + firstName + ", phoneNumber=" + phoneNumber + ", tgName=" +
                tgName + ", address=" + address + "]";
    }
}
