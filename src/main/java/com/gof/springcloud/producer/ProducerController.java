package com.gof.springcloud.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gof.springcloud.entity.Booking;
import com.gof.springcloud.entity.ResultVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api("Kafka Producer Controller")
public class ProducerController {
	private Logger log = LoggerFactory.getLogger(ProducerController.class);

	@Value(value = "${topic.name}")
	private String topic;

	@Value(value = "${topic.topic_in_1}")
    private String topic_in_1;

	@Value(value = "${topic.topic_in_2}")
    private String topic_in_2;

	@Value(value = "${topic.topic_in_3}")
    private String topic_in_3;

	@Value(value = "${topic.topic_in_4}")
    private String topic_in_4;

    @Autowired
    private KafkaTemplate<String, Booking> bookingKafkaTemplate;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

	@PostMapping("/sendBooking")
	@ApiOperation(value = "Sent a booking")
	public ResultVo<String> sendBooking(Booking booking){
		bookingKafkaTemplate.send(topic, String.valueOf(booking.getCid()), booking);
		return new ResultVo<String>(true);
	}

	@PostMapping("/sendForwardMsg")
	@ApiOperation(value = "Sent a forward msg")
	public ResultVo<String> sendForwardMsg(String msg){
		kafkaTemplate.send(topic_in_1, msg);
		return new ResultVo<String>(true);
	}

	@PostMapping("/sendWordCountMsg")
	@ApiOperation(value = "Sent a WordCount msg")
	public ResultVo<String> sendWordCountMsg(String msg){
		kafkaTemplate.send(topic_in_2, msg);
		return new ResultVo<String>(true);
	}

	@PostMapping("/sendSumMsg")
	@ApiOperation(value = "Sent a Sum msg")
	public ResultVo<String> sendSumMsg(String val){
		kafkaTemplate.send(topic_in_3, val);
		return new ResultVo<String>(true);
	}

	@PostMapping("/sendWindowAppendMsg")
	@ApiOperation(value = "Sent a Window Append msg")
	public ResultVo<String> sendWindowAppendMsg(String key, String val) {
		kafkaTemplate.send(topic_in_4, key, val);
		return new ResultVo<String>(true);
	}

}
