package com.gof.springcloud.streams;

import java.util.Arrays;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KGroupedStream;
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
public class WordCountTableDSL {

    @Value(value = "${topic.topic_in_2}")
    private String topic_in;
    @Value(value = "${topic.topic_out_2}")
    private String topic_out;

	@Value(value = "${stream.ktable:wordcount}")
	private String ktableName;

    @Bean("wordCountStream")
	public KTable<String, Long> wordCountProcessing(
			@Qualifier(KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_BUILDER_BEAN_NAME) StreamsBuilder builder) {
    	KStream<String, String> textLines = builder.stream(topic_in);
		// Define the processing topology (here: WordCount)
		final Serde<String> stringSerde = Serdes.String();
		KGroupedStream<String, String> groupedByWord = textLines
				.flatMapValues(value -> Arrays.asList(value.toLowerCase().split("\\W+")))
				.groupBy((key, word) -> word, Grouped.with(stringSerde, stringSerde));
		// Create a key-value store named "word-count" for the all-time word counts
		KTable<String, Long> stream = groupedByWord.count(Materialized
				.<String, Long, KeyValueStore<Bytes, byte[]>>as(ktableName).withValueSerde(Serdes.Long()));
		stream.toStream().foreach((k, v) -> {
			log.info("key:" + k + "   value:" + v);
		});

		stream.toStream().map((x, y) -> {
			return new KeyValue<String, String>(x, y.toString());
		}).to(topic_out);
		return stream;
	}
}
