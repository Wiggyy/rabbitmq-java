package application;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import domain.events.Type1Event;

import java.util.logging.Logger;

public class EventPublisher {
    private static final Logger logger = Logger.getLogger(EventPublisher.class.getName());

    public static void main(String[] args) {
        String queueName = Type1Event.class.getSimpleName(); 

        try {
            
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost"); 

            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {

                
                channel.queueDeclare(queueName, false, false, false, null);
                logger.info("Connected to RabbitMQ, queue: " + queueName);

                
                while (true) {
                    String message = queueName; 
                    channel.basicPublish("", queueName, null, message.getBytes());
                    logger.info("Sent: " + message);

                    Thread.sleep(5000);
                }
            }
        } catch (Exception e) {
            logger.severe("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}