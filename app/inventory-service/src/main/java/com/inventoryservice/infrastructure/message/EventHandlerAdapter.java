package com.inventoryservice.infrastructure.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventoryservice.domain.PlacedOrderEvent;
import com.inventoryservice.domain.port.EventHandlerPort;
import com.inventoryservice.domain.port.ProductUseCasePort;
import com.inventoryservice.infrastructure.message.log.MessageLog;
import com.inventoryservice.infrastructure.message.log.MessageLogRepository;
import com.inventoryservice.infrastructure.message.outbox.OutBox;
import com.inventoryservice.infrastructure.message.outbox.OutBoxRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;
import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventHandlerAdapter implements EventHandlerPort {

	private final ObjectMapper mapper;
	private final ProductUseCasePort productUseCasePort;
	private final MessageLogRepository messageLogRepository;



	private final OutBoxRepository outBoxRepository;
	    private static final String PRODUCT = "PRODUCT";

    private static final String RESERVE_CUSTOMER_BALANCE_SUCCESSFULLY = "RESERVE_CUSTOMER_BALANCE_SUCCESSFULLY";

    private static final String RESERVE_PRODUCT_STOCK_FAILED = "RESERVE_PRODUCT_STOCK_FAILED";

    private static final String RESERVE_PRODUCT_STOCK_SUCCESSFULLY = "RESERVE_PRODUCT_STOCK_SUCCESSFULLY";

	private PlacedOrderEvent deserialize(String event) {
		PlacedOrderEvent placedOrderEvent;
		try {
			placedOrderEvent = mapper.readValue(event, PlacedOrderEvent.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Couldn't deserialize event", e);
		}
		return placedOrderEvent;
	}

	private String getHeaderAsString(MessageHeaders headers, String name) {
		var value = headers.get(name, byte[].class);
		if (Objects.isNull(value)) {
			throw new IllegalArgumentException(
					String.format("Expected record header %s not present", name));
		}
		return new String(value, StandardCharsets.UTF_8);
	}

	@Bean
	@Transactional
	@Override
	public Consumer<Message<String>> handleReserveProductStockRequest() {
		return event -> {
			var messageId = event.getHeaders().getId();
			if (Objects.nonNull(messageId) && !messageLogRepository.isMessageProcessed(messageId)) {
				var eventType = getHeaderAsString(event.getHeaders(), "eventType");

				if (eventType.equals(RESERVE_CUSTOMER_BALANCE_SUCCESSFULLY)) {
                    var placedOrderEvent = deserialize(event.getPayload());

                    log.debug("Start process reserve product stock {}", placedOrderEvent);
                    var outbox = new OutBox();
                    outbox.setAggregateId(placedOrderEvent.id());
                    outbox.setAggregateType(PRODUCT);
                    outbox.setPayload(mapper.convertValue(placedOrderEvent, JsonNode.class));

                    if (productUseCasePort.reserveProduct(placedOrderEvent)) {
                        outbox.setType(RESERVE_PRODUCT_STOCK_SUCCESSFULLY);
                    } else {
                        outbox.setType(RESERVE_PRODUCT_STOCK_FAILED);
                    }

                    // Exported event into outbox table
                    outBoxRepository.save(outbox);
                    log.debug("Done process reserve product stock {}", placedOrderEvent);
                }
                // Marked message is processed
                messageLogRepository.save(new MessageLog(messageId, Timestamp.from(Instant.now())));
			}
		};
	}
}
