package com.ecom.product.service;

import com.ecom.product.dto.ProductEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductEventPublisher {

    private static final String TOPIC = "product.events";
    private final KafkaTemplate<String, ProductEvent> kafkaTemplate;

    public void publish(ProductEvent event) {
        kafkaTemplate.send(TOPIC, event.getProductId(), event);
    }


}
