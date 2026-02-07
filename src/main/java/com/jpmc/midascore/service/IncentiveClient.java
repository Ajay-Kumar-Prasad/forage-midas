package com.jpmc.midascore.service;

import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IncentiveClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${incentive.api.url}")
    private String incentiveUrl;

    public Incentive fetchIncentive(Transaction transaction) {
        return restTemplate.postForObject(
                incentiveUrl,
                transaction,
                Incentive.class
        );
    }
}
