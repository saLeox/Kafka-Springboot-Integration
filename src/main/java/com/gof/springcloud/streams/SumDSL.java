package com.gof.springcloud.streams;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class SumDSL {

    @Value(value = "${topic.topic_in_3}")
    private String topic_in;
    @Value(value = "${topic.topic_out_3}")
    private String topic_out;

	@Bean("sumStream")
	public KTable<String, String> sumProcessing(
			@Qualifier(KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_BUILDER_BEAN_NAME) StreamsBuilder builder) {
		KStream<Object, Object> source = builder.stream(topic_in);
		KTable<String, String> sumTable = source
				.map((key, value) -> new KeyValue<String, String>("sum", value.toString())).groupByKey()
				.reduce((x, y) -> {
					log.info("x: " + x + " " + "y: " + y);
					Integer sum = Integer.valueOf(x) + Integer.valueOf(y);
					log.info("sum: " + sum);
					return sum.toString();
				});

		sumTable.toStream().to(topic_out);
		return sumTable;
	}
}
