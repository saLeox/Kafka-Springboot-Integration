package com.gof.springcloud.streams;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class WindowWordCountDSL {

    @Value(value = "${topic.topic_window}")
    private String topic;


	@Bean("windowWordCountStream")
	public KStream<Object, Object> windowWordCountProcessing(
			@Qualifier(KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_BUILDER_BEAN_NAME) StreamsBuilder builder) {
		KStream<Object, Object> source = builder.stream(topic);
		Duration windowDuration = Duration.ofMillis(5000); // 10 second
		TimeWindows window = TimeWindows.of(windowDuration); // .advanceBy(windowDuration)

		source.flatMapValues((value) -> { // 返回压扁后的数据
			String[] split = value.toString().split(" "); // 对数据进行按空格切割，返回list集合
			List<String> strings = Arrays.asList(split);
			return strings;
		}) // key:null hello,null world,null hello,null java
				.map((k, v) -> {
					return new KeyValue<String, String>(v, "1");
				})
				.groupByKey()
				//.windowedBy(TimeWindows.of(Duration.ofMillis(1000)))
				//.windowedBy(window)
//		// 以下所有窗口的时间均可通过下方参数调设
//
//		 Tumbling Time Window(窗口为5秒,5秒内有效)
//                .windowedBy(TimeWindows.of(Duration.ofSeconds(5).toMillis()))
//
//		// Hopping Time Window(窗口为5秒,每次移动2秒,所以若5秒内只输入一次会出现5/2+1=3次)
////                .windowedBy(TimeWindows.of(Duration.ofSeconds(5).toMillis())
////                        .advanceBy(Duration.ofSeconds(2).toMillis()))
//
//		// Session Time Window(20秒内只要输入Session就有效,距离下一次输入超过20秒Session失效,所有从重新从0开始)
////                .windowedBy(SessionWindows.with(Duration.ofSeconds(20).toMillis()))
				.count().toStream().foreach((x, y) -> {
					log.info("x: " + x + " y:" + y);
				});
		return source;
	}
}
