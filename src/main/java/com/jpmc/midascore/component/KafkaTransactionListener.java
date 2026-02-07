package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import com.jpmc.midascore.service.IncentiveClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class KafkaTransactionListener {

    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRecordRepository;
    private final IncentiveClient incentiveClient;

    public KafkaTransactionListener(
            UserRepository userRepository,
            TransactionRecordRepository transactionRecordRepository,
             IncentiveClient incentiveClient
    ) {
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
        this.incentiveClient = incentiveClient;
    }

    @KafkaListener(
            topics = "${general.kafka-topic}",
            groupId = "midas-core-group",
            properties = { "auto.offset.reset=earliest" }
    )
    @Transactional
    public void listen(String message) throws Exception {

        Transaction transaction =
                new ObjectMapper().readValue(message, Transaction.class);
        // PUT BREAKPOINT HERE
//        System.out.println("Transaction:" + transaction.getAmount());
        Optional<UserRecord> senderOpt =
                userRepository.findById(transaction.getSenderId());

        Optional<UserRecord> recipientOpt =
                userRepository.findById(transaction.getRecipientId());

        if (senderOpt.isEmpty() || recipientOpt.isEmpty()) return;

        UserRecord sender = senderOpt.get();
        UserRecord recipient = recipientOpt.get();

        if (sender.getBalance() < transaction.getAmount()) return;

        Incentive incentive = incentiveClient.fetchIncentive(transaction);
        float incentiveAmount = incentive.amount();

        sender.setBalance(sender.getBalance() - transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentiveAmount);

        userRepository.save(sender);
        userRepository.save(recipient);

        TransactionRecord record =
                new TransactionRecord(sender, recipient, transaction.getAmount(), incentiveAmount);

        transactionRecordRepository.save(record);
    }
}
