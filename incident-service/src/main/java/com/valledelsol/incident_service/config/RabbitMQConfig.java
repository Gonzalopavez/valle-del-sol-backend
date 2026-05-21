package com.valledelsol.incident_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Nombres exactos que se acordò con geo-service para que se comuniquen correctamente
    public static final String EXCHANGE_NAME = "incident.exchange";
    public static final String QUEUE_NAME = "geo.incident.queue";
    public static final String ROUTING_KEY = "incident.validated";

    // 1. Definir el Exchange (Central de distribución)
    @Bean
    public TopicExchange incidentExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    // 2. Definir la Cola donde geo-service escuchará
    @Bean
    public Queue geoQueue() {
        return new Queue(QUEUE_NAME, true); // true para que sea durable si se reinicia el server
    }

    // 3. Unir la Cola con el Exchange usando la clave de enrutamiento
    @Bean
    public Binding bindingGeo(Queue geoQueue, TopicExchange incidentExchange) {
        return BindingBuilder.bind(geoQueue).to(incidentExchange).with(ROUTING_KEY);
    }

    // 4. Convertidor indispensable para enviar los objetos como JSON y no como bytes raros
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}