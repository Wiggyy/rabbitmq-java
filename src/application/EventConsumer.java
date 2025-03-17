package application;

import com.rabbitmq.client.*;
import domain.events.Type1Event;

import java.io.IOException;
import java.util.logging.Logger;

public class EventConsumer {
    private static final Logger logger = Logger.getLogger(EventConsumer.class.getName());

    public static void main(String[] args) {
        String queueName = Type1Event.class.getSimpleName(); 

        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

           
            channel.queueDeclare(queueName, false, false, false, null);
            logger.info("Connected to RabbitMQ, listening on queue: " + queueName);

            
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                logger.info("Received: " + message);

                try {
                    Thread.sleep(10000); 
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                logger.info("Processed: " + message);
            };

            
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});

        } catch (Exception e) {
            logger.severe("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
