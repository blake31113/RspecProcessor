/**
 * Created by Blake on 2014/4/8.
 */
import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
//http://www.jansipke.nl/websocket-tutorial-with-java-server-jetty-and-javascript-client/
@WebSocket
public class MyWebSocketHandler_140419
{
    private ArrayList<String> xmlBucket= new ArrayList<String>();
    @OnWebSocketClose
    public void onClose(int statusCode, String reason)
    {
        System.out.println("Close: statusCode=" + statusCode + ", reason=" + reason);
    }

    @OnWebSocketError
    public void onError(Throwable t)
    {
        System.out.println("Error: " + t.getMessage());
    }

    @OnWebSocketConnect
    public void onConnect(Session session)
    {
        System.out.println("Connect: " + session.getRemoteAddress().getAddress());
        try {
            session.getRemote().sendString("Hello Webbrowser");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnWebSocketMessage
    public void onMessage(String message)
    {
        String path = "C:\\";

        String fileName="";
        System.out.println("Message: " + message);
        File writeXml = new File(path+fileName);
    }
}