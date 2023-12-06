package PantryPal;

public class Link {
    String username;
    int index;
    Link (String username, int index) {
        this.username = username;
        this.index = index;
    }

    private static final String SERVER_PORT = EnvironmentHandler.loadEnv("SERVER_PORT");
    private static final String USER_HOSTNAME = EnvironmentHandler.loadEnv("USER_HOST");

    public String getHTTPLink() {
        return "http://" + USER_HOSTNAME + ":" + SERVER_PORT + "/share/" + username + "/" + index;
    }
}
