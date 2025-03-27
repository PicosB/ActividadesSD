package com.mycompany.wsockedcliente;

import jakarta.websocket.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import javax.swing.*;
import org.json.JSONObject;

@ClientEndpoint
public class ClienteSwing {

    private Session session;
    private String alias;

    private JTextArea areaMensajes = new JTextArea(10, 40);
    private JTextField campoMensaje = new JTextField(30);
    private JTextField campoAlias = new JTextField(10);
    private JTextField campoDestino = new JTextField(10);
    private JButton botonConectar = new JButton("Conectar");
    private JButton botonEnviar = new JButton("Enviar");

    public ClienteSwing() {
        JFrame frame = new JFrame("Mensajero Swing");
        JPanel panelTop = new JPanel();
        JPanel panelBottom = new JPanel();

        areaMensajes.setEditable(false);

        panelTop.add(new JLabel("Alias:"));
        panelTop.add(campoAlias);
        panelTop.add(botonConectar);

        panelBottom.add(new JLabel("Para:"));
        panelBottom.add(campoDestino);
        panelBottom.add(campoMensaje);
        panelBottom.add(botonEnviar);

        frame.add(panelTop, BorderLayout.NORTH);
        frame.add(new JScrollPane(areaMensajes), BorderLayout.CENTER);
        frame.add(panelBottom, BorderLayout.SOUTH);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        botonEnviar.setEnabled(false);

        // Eventos
        botonConectar.addActionListener(e -> conectar());
        botonEnviar.addActionListener(e -> enviarMensaje());
    }

    public void conectar() {
        alias = campoAlias.getText().trim();
        if (alias.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingresa un alias");
            return;
        }

        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI("ws://localhost:8080/wSoket/mensajero")); // cambia tuApp
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar");
        }
    }

    public void enviarMensaje() {
        String mensaje = campoMensaje.getText().trim();
        String destino = campoDestino.getText().trim();

        if (mensaje.isEmpty()) return;

        JSONObject json = new JSONObject();
        json.put("tipo", "mensaje");
        json.put("mensaje", mensaje);
        json.put("para", destino.isEmpty() ? "todos" : destino);

        session.getAsyncRemote().sendText(json.toString());

        campoMensaje.setText("");
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;

        // Enviar alias al servidor
        JSONObject json = new JSONObject();
        json.put("tipo", "registro");
        json.put("alias", alias);
        session.getAsyncRemote().sendText(json.toString());

        SwingUtilities.invokeLater(() -> {
            botonConectar.setEnabled(false);
            botonEnviar.setEnabled(true);
        });
    }

    @OnMessage
    public void onMessage(String message) {
        JSONObject json = new JSONObject(message);
        String tipo = json.optString("tipo", "");

        if (tipo.equals("usuarios")) {
            areaMensajes.append("[Usuarios]: " + json.getJSONArray("lista") + "\n");
        } else if (tipo.equals("mensaje")) {
            String de = json.getString("de");
            String para = json.getString("para");
            String texto = json.getString("mensaje");

            String prefijo = para.equals("todos") ? "[Público]" : "[Privado a " + para + "]";
            areaMensajes.append(prefijo + " " + de + ": " + texto + "\n");
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        areaMensajes.append("Conexión cerrada: " + reason.getReasonPhrase() + "\n");
    }

    @OnError
    public void onError(Session session, Throwable thr) {
        areaMensajes.append("Error: " + thr.getMessage() + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClienteSwing::new);
    }
}

