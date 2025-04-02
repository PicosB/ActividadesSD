/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.rabbitmqpubsub;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 *
 * @author xxbry
 */
public class EmitLog {
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost"); // Conectar a RabbitMQ

        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            // Declarar el exchange de tipo "fanout"
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

            // Ciclo para enviar múltiples mensajes
            for (int i = 1; i <= 5; i++) {
                String message = "Este es el mensaje #" + i;
                // Publicamos el mensaje al exchange
                channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + message + "'");
                // Simulamos un pequeño retraso antes de enviar el siguiente mensaje
                Thread.sleep(1000);  // Espera 1 segundo antes de enviar el siguiente mensaje
            }
        }
    }
}
