/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Receive;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 *
 * @author xxbry
 */
public class ReceiveLogs {
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost"); // Conectar a RabbitMQ

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // Declarar el exchange de tipo "fanout"
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        // Crear una cola temporal y enlazarla con el exchange
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println(" [*] Esperando mensajes. Presiona CTRL+C para salir.");

        // Recibir los mensajes
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Recibido: '" + message + "'");
        };

        // Escuchar la cola
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }
}
