package ws;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private final ConcurrentHashMap<SessionKey, Session> connections = new ConcurrentHashMap<>();

    public void addConnection(SessionKey key, Session session) {
        connections.put(key, session);
    }

    public void removeConnection(SessionKey key) {
        connections.remove(key);
    }

    public Session getSession(SessionKey key) {
        return connections.get(key);
    }

    public ConcurrentHashMap<SessionKey, Session> getConnections() {
        return connections;
    }

    public void sendMessage(Session client, String jsonServerMessage) {
        if (client.isOpen()) {
            try {
                client.getRemote().sendString(jsonServerMessage);
            } catch (IOException e) {
                System.err.println("Failed to send message: " + e.getMessage());
            }
        }
    }
}
