package VGDBM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.File;

/**
 * The DatabaseManager class is responsible for managing the game database.
 * It provides methods for opening, initializing, inserting, updating, removing,
 * and querying games in the database.
 */
class DatabaseManager {

    private Connection conn;

    /**
     * Opens a database with the given address and name.
     * If a database with the given name does not exist in the current directory,
     * a new database is created and initialized.
     * If a database with the given name already exists, it is opened.
     *
     * @param address the address of the directory where the database is located
     * @param name the name of the database
     * @throws SQLException if there is an error in connecting to the database
     */
    public void open(String address, String name) throws SQLException {

        boolean exists;
        File file = new File(address, name);
        String dbPath = file.getPath();
        exists = file.exists();

        if (!exists) {
            System.out.println("Creating " + name);
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            initialize(conn);
        }

        else {
            System.out.println("Opening " + name);
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        }

    }

    /**
     * Initializes the database by creating the Games table if it does not exist.
     * 
     * @param conn the database connection
     * @throws SQLException if an error occurs while executing the SQL statement
     */
    public void initialize(Connection conn) throws SQLException {

            PreparedStatement stmt = conn.prepareStatement("CREATE TABLE IF NOT EXISTS Games (Game TEXT PRIMARY KEY, Score DOUBLE, Year INTEGER, Platform TEXT, Developer TEXT, Genre TEXT, Notes TEXT);");
            stmt.executeUpdate();
            stmt.close();
            System.out.println("Table Games created"); 

    }

    /**
     * Inserts a game into the database.
     * 
     * @param game the game object to be inserted
     * @throws SQLException if there is an error executing the SQL statement
     */
    public void insertGame(Game game) throws SQLException {

        Double scoreDouble;
        Integer yearInt;

        if (game.getScore().equals("-")) {
            scoreDouble = null;
        }

        else {
            scoreDouble = Double.parseDouble(game.getScore().replace("+", ".5"));
        }

        if (game.getYear().equals("-")) {
            yearInt = null;
        }

        else {
            yearInt = Integer.parseInt(game.getYear());
        }

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Games (Game, Score, Year, Platform, Developer, Genre, Notes) VALUES (?, ?, ?, ?, ?, ?, ?);");
        stmt.setString(1, game.getGame());

        if (scoreDouble != null) {
            stmt.setDouble(2, scoreDouble);
        }

        else {
            stmt.setNull(2, java.sql.Types.DOUBLE);
        }

        if (yearInt != null) {
            stmt.setInt(3, yearInt);
        }

        else {
            stmt.setNull(3, java.sql.Types.INTEGER);
        }

        stmt.setString(4, game.getPlatform());
        stmt.setString(5, game.getDeveloper());
        stmt.setString(6, game.getGenre());
        stmt.setString(7, game.getNotes());
        stmt.executeUpdate();
        stmt.close();
        System.out.println("'" + game.getGame() + "' added to database");
    }


    /**
     * Updates a game in the database with the provided information.
     *
     * @param game         the name of the game to be updated
     * @param updatedGame  the updated game object containing the new information
     * @throws SQLException if there is an error executing the SQL statement
     */
    public void updateGame(String game, Game updatedGame) throws SQLException {

        Double scoreDouble;
        Integer yearInt;

        if (updatedGame.getScore().equals("-")) {
            scoreDouble = null;
        }

        else {
            scoreDouble = Double.parseDouble(updatedGame.getScore().replace("+", ".5"));
        }

        if (updatedGame.getYear().equals("-")) {
            yearInt = null;
        }

        else {
            yearInt = Integer.parseInt(updatedGame.getYear());
        }

        PreparedStatement stmt = conn.prepareStatement("UPDATE Games SET Game = ?, Score = ?, Year = ?, Platform = ?, Developer = ?, Genre = ?, Notes = ? WHERE Game = ?;");
        stmt.setString(1, updatedGame.getGame());

        if (scoreDouble != null) {
            stmt.setDouble(2, scoreDouble);
        }

        else {
            stmt.setNull(2, java.sql.Types.DOUBLE);
        }

        if (yearInt != null) {
            stmt.setInt(3, yearInt);
        }

        else {
            stmt.setNull(3, java.sql.Types.INTEGER);
        }

        stmt.setString(4, updatedGame.getPlatform());
        stmt.setString(5, updatedGame.getDeveloper());
        stmt.setString(6, updatedGame.getGenre());
        stmt.setString(7, updatedGame.getNotes());
        stmt.setString(8, game);
        stmt.executeUpdate();
        stmt.close();
        System.out.println("'" + game + "' updated in database");

    }

    /**
     * Removes a game from the database.
     *
     * @param game the name of the game to be removed
     * @throws SQLException if there is an error executing the SQL statement
     */
    public void removeGame(String game) throws SQLException {

        if (!gameExists(game)) {
            throw new IllegalArgumentException("'" + game + "' not in database");
        }
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Games WHERE Game = ?;");
        stmt.setString(1, game);
        stmt.executeUpdate();
        stmt.close();
        System.out.println("'" + game + "' removed from database");

    }

    /**
     * Checks if a game exists in the database.
     *
     * @param game the name of the game to check
     * @return true if the game exists, false otherwise
     * @throws SQLException if there is an error executing the SQL query
     */
    public boolean gameExists(String game) throws SQLException {

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Games WHERE Game = ?;");
        stmt.setString(1, game);
        ResultSet rs = stmt.executeQuery();
        boolean exists = rs.next();
        stmt.close();
        return exists;

    }

    /**
     * Fetches a game from the database.
     * 
     * @param game the name of the game to fetch
     * @return the game object
     * @throws SQLException if there is an error executing the SQL query
     */
    public Game fetchGame(String game) throws SQLException {

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Games WHERE Game = ?;");
        stmt.setString(1, game);
        ResultSet rs = stmt.executeQuery();
        Game fetchedGame = null;

        if (rs.next()) {
            fetchedGame = new Game(rs.getString("Game"), rs.getString("Score"), rs.getString("Year"), rs.getString("Platform"), rs.getString("Developer"), rs.getString("Genre"), rs.getString("Notes"));
        }

        stmt.close();
        return fetchedGame;

    }

    /**
     * Fetches all games from the database sorted by score in descending order.
     * 
     * @return a ResultSet containing all games in the database
     * @throws SQLException if there is an error executing the SQL query
     */
    public ResultSet fetchAllByScore() throws SQLException {

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Games ORDER BY Score DESC;");
        ResultSet rs = stmt.executeQuery();
        return rs;
    
    }

    /**
     * Fetches all games from the database sorted primarily by year in descending order,
     * secondarily by SQLite rowid in ascending order.
     * 
     * @return a ResultSet containing all games in the database
     * @throws SQLException if there is an error executing the SQL query
     */
    public ResultSet fetchAllByDate() throws SQLException {

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Games ORDER BY Year DESC, rowid ASC;");
        ResultSet rs = stmt.executeQuery();
        return rs;

    }

    /**
     * Clears the database by deleting all records from the Games table.
     * @throws SQLException if there is an error executing the SQL statement
     */
    public void clearDatabase() throws SQLException {

        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Games;");
        stmt.executeUpdate();
        stmt.close();
        System.out.println("Database cleared");

    }

    /**
     * Closes the database connection.
     * @throws SQLException if an error occurs while closing the connection.
     */
    public void close() throws SQLException {

        if (conn != null) {
            conn.close();
        }
    }
}