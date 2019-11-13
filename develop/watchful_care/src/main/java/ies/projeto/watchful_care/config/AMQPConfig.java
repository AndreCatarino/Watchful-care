package ies.projeto.watchful_care.config;

@Configuration
public class AMQPConfig {

    @Autowired
    private RabbitMQProperties rabbitMQProperties;

    @Bean
    Queue queue() {
        return new Queue(rabbitMQProperties.getQueueName(), false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(rabbitMQProperties.getExchangeName());
    }

    /*@Bean
    DirectExchange exchange() {
        return new DirectExchange(rabbitMQProperties().getExchangeName());
    }*/

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(rabbitMQProperties.getRoutingKey());
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(rabbitMQProperties.getQueueName());
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
        return new MappingJackson2MessageConverter();
    }

    @Bean
    public RabbitTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    MessageListenerAdapter listenerAdapter(RabbitMqListener listener) {
        return new MessageListenerAdapter(listener, "listen");
    }

}