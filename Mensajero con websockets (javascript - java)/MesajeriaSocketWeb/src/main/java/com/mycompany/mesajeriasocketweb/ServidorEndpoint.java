package com.mycompany.mesajeriasocketweb;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;

import jakarta.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/chat")
public class ServidorEndpoint {

    private static final CopyOnWriteArraySet<ServidorEndpoint> usuariosConectados = new CopyOnWriteArraySet<>();
    private Session session;

    // Evento cuando un usuario se conecta
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        usuariosConectados.add(this);
        System.out.println("Nuevo usuario conectado: " + session.getId());
        broadcast("Usuario " + session.getId() + " se ha unido.");
    }

    // Evento cuando se recibe un mensaje
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Mensaje de " + session.getId() + ": " + message);
        broadcast("Usuario " + session.getId() + ": " + message);
    }

    // Evento cuando un usuario se desconecta
    @OnClose
    public void onClose(Session session) {
        usuariosConectados.remove(this);
        System.out.println("Usuario desconectado: " + session.getId());
        broadcast("Usuario " + session.getId() + " se ha desconectado.");
    }

    // Evento cuando ocurre un error
    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("Error en sesión " + session.getId() + ": " + throwable.getMessage());
    }

    // Método para enviar mensajes a todos los conectados
    private void broadcast(String message) {
        for (ServidorEndpoint usuario : usuariosConectados) {
            try {
                usuario.session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                System.err.println("Error enviando mensaje: " + e.getMessage());
            }
        }
    }
}

