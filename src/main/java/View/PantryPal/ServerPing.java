package PantryPal;
import java.io.IOException;
import java.net.Socket;

public class ServerPing {
    public static boolean ping(){
        String hostname = EnvironmentHandler.loadEnv("SERVER_HOST");// changed
        int port = Integer.parseInt(EnvironmentHandler.loadEnv("SERVER_PORT")); 
        /**ping the server return true if can access */
        try (Socket socket = new Socket(hostname, port)) {
            //socket should be automatically closed here so no need to close manusaylly
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
