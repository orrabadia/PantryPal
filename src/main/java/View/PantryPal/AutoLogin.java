package PantryPal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

import java.util.ArrayList;
public class AutoLogin {
    public static void save(String user, String password){
        String filePath = "./users.csv";

        clear();
        //write saved info to csv
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(user + "," + password + "\n");
            System.out.println("User and password written to CSV successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    public static ArrayList<String> load(){
        ArrayList<String> details = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("./users.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                details.add(userData[0]);
                details.add(userData[1]);
            }
        } catch (IOException e) {
            System.out.println("Error reading from CSV file: " + e.getMessage());
        }

        return details;
    }

    public static void clear(){
        String filePath = "./users.csv";

        File file = new File(filePath);

        // check if file exists and if so delete it
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Existing file deleted");
            } else {
                System.out.println("Failed to delete users file");
                return;
            }
        }
    }
}
