package com.gof.springcloud.streams;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

@Configuration
@EnableKafkaStreams
public class KafkaStreamsConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

	@Value(value = "${topic.topic_in_1}")
	private String topic_in;
	@Value(value = "${topic.topic_out_1}")
	private String topic_out;

    // SER/DES可以在builder中再次进行设置
    // 没有必要因为不同类型创建多个StreamsBuilderFactoryBean,何况它是全局只有能唯一bean存在
    // 如果需要功能拓展则可进行改写
    // refer to 2.14
    // https://docs.spring.io/spring-cloud-stream-binder-kafka/docs/3.0.10.RELEASE/reference/html/spring-cloud-stream-binder-kafka.html#_streamsbuilderfactorybean_customizer
    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kStreamsConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "testStreams");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        //props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class.getName());
        //props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        //props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        return new KafkaStreamsConfiguration(props);
    }

//	@Bean("topicForwardStream")
//	public KStream<String, String> topicForwardProcessing(StreamsBuilder builder) {
//		final Serde<String> stringSerde = Serdes.String();
//		KStream<String, String> stream = builder.stream(topic_in, Consumed.with(stringSerde, stringSerde));
//		stream.to(topic_out);
//		return stream;
//	}

//    @Bean(name = "commonDSLBuilder")
//	public FactoryBean<StreamsBuilder> commonDSLBuilder() {
//		Map<String, Object> props = new HashMap<>();
//		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "testStreams");
//		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
//		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
//		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
//		props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class.getName());
//		StreamsBuilderFactoryBean streamsBuilder = new StreamsBuilderFactoryBean(new KafkaStreamsConfiguration(props));
//		return streamsBuilder;
//	}

}