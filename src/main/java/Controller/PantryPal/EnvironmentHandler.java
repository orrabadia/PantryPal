package PantryPal;

import java.io.*;
import java.util.Properties;

public class EnvironmentHandler {

    public static String loadEnv(String variable) {
        try {
            String configFilePath = "./config.properties";
            FileInputStream propsInput = new FileInputStream(configFilePath);
            Properties prop = new Properties();
            prop.load(propsInput);

            if (variable.equals("SERVER_PORT")){
                String PORT = prop.getProperty("SERVER_PORT");
                return PORT;
            }
            if (variable.equals("SERVER_HOST")){
                String HOST = prop.getProperty("SERVER_HOST");
                return HOST;
            }
            if (variable.equals("USER_HOST")){
                String UHOST = prop.getProperty("USER_HOST");
                return UHOST;
            }
            
        } catch (FileNotFoundException e) {
            return "FILENOTFOUNDEXCEPTION" + e.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "IOEXCEPTION" + e.toString();
        }
        return "ENVIRONMENT RETRIEVED";
    }
}

