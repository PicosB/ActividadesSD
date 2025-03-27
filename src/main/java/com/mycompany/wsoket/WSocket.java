package com.mycompany.wsoket;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.*;
import org.json.JSONObject;

@ServerEndpoint("/mensajero")
public class WSocket {

    private static final Map<Session, String> usuarios = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        // Al conectarse, aún no tiene alias. Esperamos que lo mande como primer mensaje.
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            // Mensaje esperado en formato JSON simple: {"tipo":"registro", "alias":"Juan"}
            // o {"tipo":"mensaje", "mensaje":"Hola", "para":"alias"} (si para es null o vacío, es público)

            var json = new org.json.JSONObject(message); // puedes usar cualquier lib JSON

            String tipo = json.getString("tipo");

            switch (tipo) {
                case "registro":
                    String alias = json.getString("alias");
                    usuarios.put(session, alias);
                    enviarListaUsuarios();
                    break;

                case "mensaje":
                    String texto = json.getString("mensaje");
                    String destino = json.optString("para", "");
                    String remitente = usuarios.get(session);
                    if (destino.isEmpty()) {
                        // mensaje público en JSON
                        JSONObject jsonMsg = new JSONObject();
                        jsonMsg.put("tipo", "mensaje");
                        jsonMsg.put("de", remitente);
                        jsonMsg.put("mensaje", texto);
                        jsonMsg.put("para", "todos");

                        for (Session s : usuarios.keySet()) {
                            s.getBasicRemote().sendText(jsonMsg.toString());
                        }
                    } else {
                        // mensaje privado en JSON
                        JSONObject jsonMsg = new JSONObject();
                        jsonMsg.put("tipo", "mensaje");
                        jsonMsg.put("de", remitente);
                        jsonMsg.put("mensaje", texto);
                        jsonMsg.put("para", destino);

                        for (Map.Entry<Session, String> entry : usuarios.entrySet()) {
                            if (entry.getValue().equals(destino) || entry.getValue().equals(remitente)) {
                                entry.getKey().getBasicRemote().sendText(jsonMsg.toString());
                            }
                        }
                    }

                    break;
            }

        } catch (Exception ex) {
            Logger.getLogger(WSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @OnClose
    public void onClose(Session session) {
        usuarios.remove(session);
        enviarListaUsuarios();
    }

    private void enviarListaUsuarios() {
        try {
            List<String> aliasList = new ArrayList<>(usuarios.values());
            var json = new org.json.JSONObject();
            json.put("tipo", "usuarios");
            json.put("lista", aliasList);

            for (Session s : usuarios.keySet()) {
                s.getBasicRemote().sendText(json.toString());
            }

        } catch (IOException ex) {
            Logger.getLogger(WSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
