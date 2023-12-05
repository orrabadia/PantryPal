package PantryPal;
import com.sun.net.httpserver.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class Server {
    // initialize server port and hostname
    private static final int SERVER_PORT = 8100;
    private static final String SERVER_HOSTNAME = "localhost";

  public static void main(String[] args) throws IOException {

    // create a thread pool to handle requests
    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

    // create a map to store data
    Map<String, String> data = new HashMap<>();

    // imagedisplayhandler to handle images and track if they are already generated
    ImageDisplayHandler i = new ImageDisplayHandler();


    // create a server
    HttpServer server = HttpServer.create(
      new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT),
      0
    );

    //HttpContext context = server.createContext("/", new RequestHandler(data));
    HttpContext recipecontext = server.createContext("/recipe", new RecipeRequestListener(data, i));
    HttpContext audiocontext = server.createContext("/audio", new AudioRequestListener(data));
    HttpContext generatecontext = server.createContext("/generate", new GenerateRequestListener());
    HttpContext usercontext = server.createContext("/user", new UserRequestListener(data));
    HttpContext imagecontext = server.createContext("/image", new ImageRequestListener(i));
    HttpContext sharecontext = server.createContext("/share", new ShareRequestListener(i));
    
    //add new handlers for each type
    server.setExecutor(threadPoolExecutor); 
    server.start();

    System.out.println("Server started on port " + SERVER_PORT);

  }
}
