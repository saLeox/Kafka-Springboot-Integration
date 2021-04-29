package com.gof.springcloud.streams;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class SumTableDSL {

	@Value(value = "${topic.topic_in_3}")
	private String topic_in;
	@Value(value = "${topic.topic_out_3}")
	private String topic_out;

	@Value(value = "${stream.ktable:sum}")
	private String ktableName;

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
				}, Materialized.<String, String, KeyValueStore<Bytes, byte[]>>as(ktableName)
						.withValueSerde(Serdes.String()));

		sumTable.toStream().to(topic_out);
		return sumTable;
	}
}
