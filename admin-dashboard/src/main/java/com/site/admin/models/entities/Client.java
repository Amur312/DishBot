package com.site.admin.models.entities;

import java.util.Objects;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

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
    @Column(name = "address_client")
    private String address;

    public Client() {
    }

}
