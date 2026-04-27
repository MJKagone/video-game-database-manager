package VGDBM;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.sql.SQLException;

/*  
 * CSVToSqliteFactory.java
 * Reads in a CSV file and creates a new SQLite database with the same data.
 */
class CSVToSQLite {

    /**
     * Reads a CSV file and inserts the data into a SQLite database.
     * 
     * @param address the file path of the CSV file to be read
     * @throws FileNotFoundException if the specified file path is invalid or the file does not exist
     */
    public void readCSV(String address, DatabaseManager db) throws FileNotFoundException {

        File file = new File(address);
        Scanner scanner = new Scanner(file);
        scanner.useDelimiter("\n");

        while (scanner.hasNext()) {
            String line = scanner.next();
            String[] parts = line.split(",");
            String game = parts[0];
            String score = parts[1];
            String year = parts[2];
            String platform = parts[3];
            String developer = parts[4];
            String genre = parts[5];
            String notes = parts[6];
            try {
                String notes2 = parts[7];
                notes = notes + ", " + notes2;
            } // Handles one "," character in the notes field
            catch (ArrayIndexOutOfBoundsException e) {
                
            }
            Game newGame = new Game(game, score, year, platform, developer, genre, notes);
            if (!newGame.getGame().equals("Game")) {
                try {
                    db.insertGame(newGame);
                }
                catch (SQLException e) {
                    System.out.println("SQLException");
                }
            }
        }
        scanner.close();
    }

    public void readCSVInverted(String address, DatabaseManager db) throws FileNotFoundException {
        File file = new File(address);
        Scanner scanner = new Scanner(file);
        scanner.useDelimiter("\n");

        // Read all lines into a List
        List<String> lines = new ArrayList<>();
        while (scanner.hasNext()) {
            lines.add(scanner.next());
        }
        scanner.close();

        // Iterate through the List in reverse order
        ListIterator<String> iterator = lines.listIterator(lines.size());
        while (iterator.hasPrevious()) {
            String line = iterator.previous();
            String[] parts = line.split(",");
            String game = parts[0];
            String score = parts[1];
            String year = parts[2];
            String platform = parts[3];
            String developer = parts[4];
            String genre = parts[5];
            String notes = parts[6];
            Game newGame = new Game(game, score, year, platform, developer, genre, notes);
            if (!newGame.getGame().equals("Game")) {
                try {
                    db.insertGame(newGame);
                } catch (SQLException e) {
                    System.out.println("SQLException");
                }
            }
        }
    }

    public static void main(String[] args) {

        DatabaseManager db = new DatabaseManager();
        String CSVAddress = "." + File.separator + "games.csv";

        System.out.println("Invert the order of the CSV file? (y/n)");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();

        try {
            db.open(".", "games.db");
            db.clearDatabase();
            CSVToSQLite factory = new CSVToSQLite();
            if (choice.equals("y")) {
                factory.readCSVInverted(CSVAddress, db);
            }
            else if (choice.equals("n")) {
                factory.readCSV(CSVAddress, db);
            }
            else {
                System.out.println("Invalid input");
            }
            db.close();
        }

        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}