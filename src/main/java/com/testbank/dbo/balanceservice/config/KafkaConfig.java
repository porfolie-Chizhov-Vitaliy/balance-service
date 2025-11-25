package com.testbank.dbo.balanceservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
@Configuration
public class KafkaConfig {


    @Bean
    public NewTopic balanceChecksTopic() {
        return TopicBuilder.name("balance-checks")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic paymentResultsTopic() {
        return TopicBuilder.name("payment-results")
                .partitions(3)
                .replicas(1)
                .build();
    }
    @Bean
    public NewTopic notifyEventsTopic() {
        return TopicBuilder.name("notify-events")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
