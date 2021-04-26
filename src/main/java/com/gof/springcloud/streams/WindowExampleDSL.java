package com.gof.springcloud.streams;

import java.time.Duration;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class WindowExampleDSL {

    @Value(value = "${topic.topic_in_4}")
    private String topic_in;
    @Value(value = "${topic.topic_out_4}")
    private String topic_out;

	@Bean("windowExampleStream")
	public KStream<String, String> windowExampleProcessing(
			@Qualifier(KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_BUILDER_BEAN_NAME) StreamsBuilder builder) {
		KStream<String, String> stream = builder.stream(topic_in);
		stream.mapValues((ValueMapper<String, String>) String::toUpperCase).groupByKey()
				.windowedBy(TimeWindows.of(Duration.ofMillis(100000)))
				.reduce((String value1, String value2) -> value1 + value2, Named.as("windowStore")).toStream()
				.map((windowedId, value) -> new KeyValue<>(windowedId.key(), value)).filter((i, s) -> s.length() > 40)
				.to(topic_out);

		stream.print(Printed.toSysOut());

		return stream;

	}
}
