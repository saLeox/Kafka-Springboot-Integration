package com.gof.springcloud.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.gof.springcloud.entity.Booking;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Component
public class KafkaConsumer {

	@KafkaListener(topics = "${topic.name}", groupId = "${topic.group}", containerFactory = "bookingKafkaListenerContainerFactory")
	public void consumeJson(Booking booking) {
		log.info("Consumed JSON Message: " + booking);
	}

	@KafkaListener(topics = "${topic.topic_out_1}", groupId = "${topic.group}")
	public void consumeJson(String msg) {
		log.info("Consumed JSON Message: " + msg);
	}
}
