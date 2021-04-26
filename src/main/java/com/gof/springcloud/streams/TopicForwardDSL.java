package com.gof.springcloud.streams;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;

@Configuration
public class TopicForwardDSL {

	@Value(value = "${topic.topic_in_1}")
	private String topic_in;
	@Value(value = "${topic.topic_out_1}")
	private String topic_out;

	@Bean("topicForwardStream")
	public KStream<String, String> topicForwardProcessing(
			@Qualifier(KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_BUILDER_BEAN_NAME) StreamsBuilder builder) {
		final Serde<String> stringSerde = Serdes.String();
		KStream<String, String> stream = builder.stream(topic_in, Consumed.with(stringSerde, stringSerde));		stream.to(topic_out);
		return stream;
	}
}
