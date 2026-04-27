package VGDBM;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * CSVToSqliteFactory.java
 * Reads in an SQLite database and creates a new CSV file with the same data sorted by date.
 */
class SQLiteToCSV {

    private DatabaseManager db;

    SQLiteToCSV(DatabaseManager db) {
        this.db = db;
    }

    /**
     * Reads an SQLite database and writes the data to a CSV file.
     * 
     * @param address the file path of the CSV file to be written
     * @throws FileNotFoundException if the specified file path is invalid
     */
    public void writeCSV(String address) throws FileNotFoundException {

        File file = new File(address);
        PrintWriter writer = new PrintWriter(file);

        try {

            String game;
            String score;
            String year;
            String platform;
            String developer;
            String genre;
            String notes;
            ResultSet rs = db.fetchAllByDate();
            writer.print("Game,Score,Year,Platform,Genre(s),Notes\n");

            while (rs.next()) {
                
                game = rs.getString("Game");

                if (rs.getString("Score") == null) {
                    score = "-";
                }
                else if (rs.getString("Score").contains(".5")) {
                    score = rs.getString("Score").substring(0, 1) + "+";
                }
                else if (rs.getString("Score").contains(".0")) {
                    score = rs.getString("Score").substring(0, 1);
                }
                else {
                    score = rs.getString("Score");
                }

                if (rs.getString("Year") == null) {
                    year = "-";
                } 
                else {
                    year = rs.getString("Year");
                }
                
                platform = rs.getString("Platform");
                developer = rs.getString("Developer");
                genre = rs.getString("Genre");
                notes = rs.getString("Notes");
                writer.print(game + "," + score + "," + year + "," + platform + "," + developer + "," + genre + "," + notes + "\n");
            }
        }

        catch (SQLException e) {
            System.out.println("SQLException");
        }

        writer.close();
        
    }
}