

# Kafka practice on Springboot

## *Highlight*

### 4 main sections, 4/4 now

 - [x] 1. [producer](https://github.com/saLeox/springboot-kafka-streams/tree/main/src/main/java/com/gof/springcloud/producer)
	
	Allow customized class to send topic.
 - [x]  2. [consumer](https://github.com/saLeox/springboot-kafka-streams/tree/main/src/main/java/com/gof/springcloud/consumer)
	
	Allow customized class to receive topic.
 - [x]  3. [streams](https://github.com/saLeox/springboot-kafka-streams/tree/main/src/main/java/com/gof/springcloud/streams) (including windows)

 - Perform Transformations and Actions. 	 
 - Materialize the KTable.
 - Transfer to other topic.
 - Handover Topology management to spring and only need to configure the connection info in one [place](https://github.com/saLeox/springboot-kafka-streams/blob/main/src/main/java/com/gof/springcloud/streams/KafkaStreamsConfig.java).
 - [x] 4. [interactiveQuery](https://github.com/saLeox/springboot-kafka-streams/tree/main/src/main/java/com/gof/springcloud/interactiveQuery)
	
	Get the KafkaStreams by bean injection in Spring, refer to [here](https://github.com/saLeox/springboot-kafka-streams/blob/main/src/main/java/com/gof/springcloud/streams/query/InteractiveQueryController.java).
	
	
*Spark-streaming* or *Flink-streaming* need cluster to submit jobs, will not make any attempt so far.

## *Before you deploy*
Prepare the kafka cluster, can follow the instruction [here](https://github.com/saLeox/kafka-cluster-docker-usage) and run on the top of docker.


## *After you deploy*
The producer and streaming-interactive-query modules are open on Swagger page once you start this project.
![](https://raw.githubusercontent.com/saLeox/photoHub/main/20210429203451.png)
