package com.wine.to.up.user.service.configuration;

import com.wine.to.up.catalog.service.api.CatalogServiceApiProperties;
import com.wine.to.up.catalog.service.api.message.UpdatePriceEventOuterClass.UpdatePriceEvent;
import com.wine.to.up.commonlib.messaging.BaseKafkaHandler;
import com.wine.to.up.commonlib.messaging.KafkaMessageSender;
import com.wine.to.up.user.service.api.UserServiceApiProperties;
import com.wine.to.up.user.service.api.message.WineReviewedEventOuterClass.WineReviewedEvent;
import com.wine.to.up.user.service.components.UserServiceMetricsCollector;
import com.wine.to.up.user.service.messaging.UpdatePriceHandler;
import com.wine.to.up.user.service.messaging.serialization.EventDeserializer;
import com.wine.to.up.user.service.messaging.serialization.EventSerializer;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class KafkaConfiguration {
    /**
     * List of kafka servers
     */
    @Value("${spring.kafka.bootstrap-server}")
    private String brokers;

    /**
     * Application consumer group id
     */
    @Value("${spring.kafka.consumer.group-id}")
    private String applicationConsumerGroupId;

    /**
     * Creating general producer properties. Common for all the producers
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Properties producerProperties() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        return properties;
    }

    /**
     * Creating general consumer properties. Common for all the consumers
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    Properties consumerProperties() {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, applicationConsumerGroupId);
        //in case of consumer crashing, new consumer will read all messages from committed offset
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, OffsetResetStrategy.EARLIEST.name().toLowerCase());
        return properties;
    }

    /**
     * Creates sender based on general properties. It helps to send single message to designated topic.
     * <p>
     * Uses custom serializer as the messages within single topic should be the same type. And
     * the messages in different topics can have different types and require different serializers
     *
     * @param producerProperties       is the general producer properties. {@link #producerProperties()}
     * @param userServiceApiProperties class containing the values of the given service's API properties (in this particular case topic name)
     * @param metricsCollector         class encapsulating the logic of the metrics collecting and publishing
     */
    @Bean
    KafkaMessageSender<WineReviewedEvent> wineReviewedEventSender(
        Properties producerProperties,
        UserServiceApiProperties userServiceApiProperties,
        UserServiceMetricsCollector metricsCollector
    ) {
        return new KafkaMessageSender<>(
            new KafkaProducer<>(producerProperties),
            userServiceApiProperties.getWineResponseTopic(),
            metricsCollector
        );
    }

    @Bean
    BaseKafkaHandler<UpdatePriceEvent> updatePriceEventHandler(
            Properties consumerProperties,
            CatalogServiceApiProperties catalogServiceApiProperties,
            UpdatePriceHandler messageHandler
    ) {
        return new BaseKafkaHandler<>(
            catalogServiceApiProperties.getEventTopic(),
            new KafkaConsumer<>(consumerProperties),
            messageHandler
        );
    }
}
