package com.gof.springcloud.streams;

import java.util.Arrays;
import java.util.List;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class WordCountDSL {

    @Value(value = "${topic.topic_in_2}")
    private String topic_in;
    @Value(value = "${topic.topic_out_2}")
    private String topic_out;

    @Bean("wordCountStream")
	public KTable<String, Long> wordCountProcessing(
			@Qualifier(KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_BUILDER_BEAN_NAME) StreamsBuilder builder) {
		KTable<String, Long> stream = builder.stream(topic_in) // 从kafka中一条一条的取数据
				.flatMapValues((value) -> { // 返回压扁后的数据
					String[] split = value.toString().split(" "); // 对数据进行按空格切割，返回list集合
					List<String> strings = Arrays.asList(split);
					return strings;
				}) // key:null hello,null world,null hello,null java
				.map((k, v) -> {
					return new KeyValue<String, String>(v, "1");
				}).groupByKey().count();

		stream.toStream().foreach((k, v) -> {
			log.info("key:" + k + "   value:" + v);
		});

		stream.toStream().map((x, y) -> {
			return new KeyValue<String, String>(x, y.toString());
		}).to(topic_out);

		return stream;
	}
}
