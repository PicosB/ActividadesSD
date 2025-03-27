/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clienteswing;


import javax.swing.*;
import java.net.URI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshake;

public class ClienteSwing extends JFrame {
    
    private JTextArea textArea; // Área para mostrar mensajes
    private JTextField textField; // Campo para escribir mensajes
    private WebSocketClient webSocketClient;

    public ClienteSwing() {
        // Configuración básica de la ventana de la aplicación Swing
        setTitle("Cliente WebSocket");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        textField = new JTextField();
        add(textField, BorderLayout.SOUTH);

        // Botón para enviar mensajes
        JButton btnSend = new JButton("Enviar");
        add(btnSend, BorderLayout.EAST);
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage(textField.getText());
                textField.setText("");
            }
        });

        // Crear el cliente WebSocket
        try {
            webSocketClient = new WebSocketClient(new URI("ws://localhost:8080/MesajeriaSocketWeb/chat")) {
                
                @Override
                public void onOpen(ServerHandshake handshake) {
                    System.out.println("Conexión establecida");
                    textArea.append("Conexión establecida\n");
                }

                @Override
                public void onMessage(String message) {
                    System.out.println("Mensaje recibido: " + message);
                    textArea.append("Otro: " + message + "\n");
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("Conexión cerrada. Código: " + code + ", Razón: " + reason);
                    textArea.append("Conexión cerrada. Código: " + code + ", Razón: " + reason + "\n");
                }

                @Override
                public void onError(Exception ex) {
                    ex.printStackTrace();
                }
            };

            // Conectar al WebSocket
            webSocketClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para enviar un mensaje al servidor WebSocket
    private void sendMessage(String message) {
        if (webSocketClient != null && webSocketClient.isOpen()) {
            webSocketClient.send(message);
            textArea.append("Tú: " + message + "\n");
        } else {
            textArea.append("No se pudo enviar el mensaje. Conexión cerrada.\n");
        }
    }

    public static void main(String[] args) {
        // Inicializar la interfaz gráfica
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ClienteSwing cliente = new ClienteSwing();
                cliente.setVisible(true);
            }
        });
    }
}

