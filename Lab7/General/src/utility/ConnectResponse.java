package utility;

public class ConnectResponse {
    private final String connectMessage;
    private final boolean clientConnected;

    public ConnectResponse(String connectMessage, boolean clientConnected) {
        this.connectMessage = connectMessage;
        this.clientConnected = clientConnected;
    }

    public String getConnectMessage() {
        return connectMessage;
    }

    public boolean isClientConnected() {
        return clientConnected;
    }
}
