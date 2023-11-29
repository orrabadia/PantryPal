package PantryPal;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

import java.util.ArrayList;
public class AutoLogin {
    public static void save(String user, String password){
        String filePath = "./users.csv";
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
}
