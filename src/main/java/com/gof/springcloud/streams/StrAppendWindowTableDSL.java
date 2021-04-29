package com.gof.springcloud.streams;

import java.time.Duration;

import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.apache.kafka.streams.state.WindowStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class StrAppendWindowTableDSL {

	@Value(value = "${topic.topic_in_4}")
	private String topic_in;
	@Value(value = "${topic.topic_out_4}")
	private String topic_out;

	@Value(value = "${stream.ktable:timewindow}")
	private String ktableNameTimeWindow;

	@Bean("windowExampleStream")
	// By default, window here is Tumbling.
	// The other two can be set specifically
	// https://kafka.apache.org/28/documentation/streams/developer-guide/dsl-api.html#hopping-time-windows
	public KStream<String, String> windowExampleProcessing(
			@Qualifier(KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_BUILDER_BEAN_NAME) StreamsBuilder builder) {
		KStream<String, String> stream = builder.stream(topic_in);
		stream.mapValues((ValueMapper<String, String>) String::toUpperCase).groupByKey()
				.windowedBy(TimeWindows.of(Duration.ofSeconds(60)))
				.reduce((String value1, String value2) -> value1 + value2,
						Materialized.<String, String, WindowStore<Bytes, byte[]>>as(ktableNameTimeWindow))
				.toStream().map((windowedId, value) -> new KeyValue<>(windowedId.key(), value))
				.filter((i, s) -> s.length() > 40).to(topic_out);

		return stream;

	}

	// Hopping time window: Fixed-size, overlapping windows
	// Tumbling time window: Fixed-size, non-overlapping, gap-less windows
	// Sliding time window: Fixed-size, overlapping windows that work on differences between record timestamps
	// Session window: Dynamically-sized, non-overlapping, data-driven windows

}
