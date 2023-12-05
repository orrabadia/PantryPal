package PantryPal;

public class Link {
    String username;
    int index;
    Link (String username, int index) {
        this.username = username;
        this.index = index;
    }
    public String getHTTPLink() {
        return "http://localhost:8100/share/" + username + "/" + index;
    }
}
