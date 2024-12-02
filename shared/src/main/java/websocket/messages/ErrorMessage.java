package websocket.messages;

public class ErrorMessage extends ServerMessage {
    public String message;

    public ErrorMessage(String message) {
        super(ServerMessage.ServerMessageType.NOTIFICATION);
        this.message = message;
    }
}
