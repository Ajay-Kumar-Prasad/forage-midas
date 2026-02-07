package com.jpmc.midascore.entity;

import jakarta.persistence.*;
import org.springframework.transaction.annotation.Transactional;

@Entity
public class TransactionRecord {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private UserRecord sender;
    @ManyToOne
    private UserRecord recipient;

    private float amount;

    @Column(nullable = false)
    private float incentive;


    public TransactionRecord() {
    }

    public TransactionRecord(
            UserRecord sender,
            UserRecord recipient,
            float amount,
            float incentive
    ) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.incentive = incentive;
    }

}
