package com.gof.springcloud.streams.query;

import java.time.Instant;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.kafka.streams.state.ReadOnlyWindowStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api("Interactive Query Control")
public class InteractiveQueryController {

	@Autowired
	@Qualifier(KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_BUILDER_BEAN_NAME)
	private StreamsBuilderFactoryBean factoryBean;

	@Value(value = "${stream.ktable:wordcount}")
	private String ktableNameWordcount;

	@Value(value = "${stream.ktable:sum}")
	private String ktableNameSum;

	@Value(value = "${stream.ktable:timewindow}")
	private String ktableNameTimeWindow;

	@GetMapping("/getWordcountByKey")
	@ApiOperation(value = "get Wordcount By Key")
	public Long getWordcountByKey(String key) {
		StoreQueryParameters<ReadOnlyKeyValueStore<String, Long>> param = StoreQueryParameters
				.fromNameAndType(ktableNameWordcount, QueryableStoreTypes.keyValueStore());
		ReadOnlyKeyValueStore<String, Long> keyValueStore = factoryBean.getKafkaStreams().store(param);
		return keyValueStore.get(key);
	}

	@GetMapping("/getSum")
	@ApiOperation(value = "get Sum")
	public String getSum() {
		StoreQueryParameters<ReadOnlyKeyValueStore<String, String>> param = StoreQueryParameters
				.fromNameAndType(ktableNameSum, QueryableStoreTypes.keyValueStore());
		ReadOnlyKeyValueStore<String, String> keyValueStore = factoryBean.getKafkaStreams().store(param);
		return keyValueStore.get("sum");
	}

	@GetMapping("/getAppendedStr")
	@ApiOperation(value = "getAppendedStr")
	public String getAppendedStr(String key) {
		StoreQueryParameters<ReadOnlyWindowStore<String, String>> param = StoreQueryParameters
				.fromNameAndType(ktableNameTimeWindow, QueryableStoreTypes.windowStore());
		ReadOnlyWindowStore<String, String> windowStore = factoryBean.getKafkaStreams().store(param);
		Instant timeTo = Instant.now(); // now (in processing-time)
		Instant timeFrom = timeTo.minusSeconds(60); // beginning of time = oldest available
		KeyValueIterator<Long, String> iterator = windowStore.fetch(key, timeFrom, timeTo);
		String res = null;
		while (iterator.hasNext()) {
			KeyValue<Long, String> next = iterator.next();
			long windowTimestamp = next.key;
			System.out.println("Count of 'world' @ time " + windowTimestamp + " is " + next.value);
			res = next.value;
		}
		return res;
	}

}
