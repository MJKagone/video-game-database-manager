/**
 * This class contains unit tests for the DatabaseManager class.
 */

 package VGDBM;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class DatabaseManagerTest {

    DatabaseManager db;
    String address;
    String fileName;

    @Before
    public void setUp() {
        db = new DatabaseManager();
        address = ".";
        fileName = "testGames.sqlite";
        try {
            db.open(address, fileName);
        } catch (Exception e) {
            fail();
        }
    }

    
    @Test
    public void insertGame() {

        try {
            Game game1 = new Game("Testgame", "TestScore", "TestDate", "TestPlatform", "TestDeveloper", "TestGenre", "Testnotes");
            Game game2 = new Game("Testgame2", "TestScore", "2020", "TestPlatform", "TestDeveloper", "TestGenre", "Testnotes2");
            Game game3 = new Game("Testgame3", "5", "TestDate", "TestPlatform", "TestDeveloper", "TestGenre", "Testnotes3");
            assertThrows(IllegalArgumentException.class, () -> db.insertGame(game1));
            assertThrows(IllegalArgumentException.class, () -> db.insertGame(game2));
            assertThrows(IllegalArgumentException.class, () -> db.insertGame(game3));
            Game game4 = new Game("Testgame4", "5", "2020", "TestPlatform", "Me", "TestGenre", "Testnotes4");
            Game game5 = new Game("Testgame5", "-", "2021", "PS5", "Me", "Action", "Testnotes5");
            Game game6 = new Game("Testgame6", "5", "-", "TestPlatform", "Me", "Action|Adventure", "Testnotes6");
            Game game7 = new Game("Testgame7", "-", "-", "-", "-", "-", "Testnotes7");
            db.insertGame(game4);
            db.insertGame(game5);
            db.insertGame(game6);
            db.insertGame(game7);
            assertTrue(db.gameExists("Testgame4"));
            assertTrue(db.gameExists("Testgame5"));
            assertTrue(db.gameExists("Testgame6"));
            assertTrue(db.gameExists("Testgame7"));
            Game insertedGame4 = db.fetchGame("Testgame4");
            Game insertedGame5 = db.fetchGame("Testgame5");
            Game insertedGame6 = db.fetchGame("Testgame6");
            Game insertedGame7 = db.fetchGame("Testgame7");
            assertTrue(insertedGame4.getGame().equals("Testgame4"));
            assertTrue(insertedGame4.getScore().equals("5.0"));
            assertTrue(insertedGame4.getYear().equals("2020"));
            assertTrue(insertedGame4.getPlatform().equals("TestPlatform"));
            assertTrue(insertedGame4.getDeveloper().equals("Me"));
            assertTrue(insertedGame4.getGenre().equals("TestGenre"));
            assertTrue(insertedGame4.getNotes().equals("Testnotes4"));
            assertTrue(insertedGame5.getGame().equals("Testgame5"));
            assertTrue(insertedGame5.getScore() == null);
            assertTrue(insertedGame5.getYear().equals("2021"));
            assertTrue(insertedGame5.getPlatform().equals("PS5"));
            assertTrue(insertedGame5.getDeveloper().equals("Me"));
            assertTrue(insertedGame5.getGenre().equals("Action"));
            assertTrue(insertedGame5.getNotes().equals("Testnotes5"));
            assertTrue(insertedGame6.getGame().equals("Testgame6"));
            assertTrue(insertedGame6.getScore().equals("5.0"));
            assertTrue(insertedGame6.getYear() == null);
            assertTrue(insertedGame6.getPlatform().equals("TestPlatform"));
            assertTrue(insertedGame6.getDeveloper().equals("Me"));
            assertTrue(insertedGame6.getGenre().equals("Action|Adventure"));
            assertTrue(insertedGame6.getNotes().equals("Testnotes6"));
            assertTrue(insertedGame7.getGame().equals("Testgame7"));
            assertTrue(insertedGame7.getScore() == null);
            assertTrue(insertedGame7.getYear() == null);
            assertTrue(insertedGame7.getPlatform().equals("-"));
            assertTrue(insertedGame7.getDeveloper().equals("-"));
            assertTrue(insertedGame7.getGenre().equals("-"));
            assertTrue(insertedGame7.getNotes().equals("Testnotes7"));
            
        } catch (SQLException e) {
            fail();
            System.out.println("Error running tests" + e.getMessage());
        }
    }

    @Test
    public void updateGame() {

        try {
            Game game = new Game("Testgame", "5", "2020", "Testplatform", "TestDeveloper", "TestGenre", "Testnotes");
            db.insertGame(game);
            Game updatedGame = new Game("Updategame", "0", "2021", "Switch", "Me", "Action|Adventure", "Testnotes2");
            db.updateGame("Testgame", updatedGame);
            assertTrue(db.gameExists("Updategame"));
            Game insertedGame = db.fetchGame("Updategame");
            assertTrue(insertedGame.getGame().equals("Updategame"));
            assertTrue(insertedGame.getScore().equals("0.0"));
            assertTrue(insertedGame.getYear().equals("2021"));
            assertTrue(insertedGame.getPlatform().equals("Switch"));
            assertTrue(insertedGame.getDeveloper().equals("Me"));
            assertTrue(insertedGame.getGenre().equals("Action|Adventure"));
            assertTrue(insertedGame.getNotes().equals("Testnotes2"));
        } catch (Exception e) {
            fail();
            System.out.println("Error running tests" + e.getMessage());
        }
    }

    @Test
    public void removeGame() {

        try {
            Game game = new Game("Deletedgame", "5", "2020", "Testplatform", "TestDeveloper", "TestGenres", "Testnotes");
            db.insertGame(game);
            db.removeGame("Deletedgame");
            assertTrue(!db.gameExists("Deleted"));
            String nonExistingGame = "Nonexistinggame";
            assertThrows(IllegalArgumentException.class, () -> db.removeGame(nonExistingGame));
        } catch (Exception e) {
            fail();
            System.out.println("Error running tests" + e.getMessage());
        }
    }

    @Test
    public void nonexistingGames() {

        try {
            assertTrue(!db.gameExists("Nonexistinggame"));
            Game notAGame = db.fetchGame("Nonexistinggame");
            assertTrue(notAGame == null);
        } catch (Exception e) {
            fail();
            System.out.println("Error running tests" + e.getMessage());
        }
    }

    @After
    public void tearDown() {

        try {
            db.clearDatabase();
            db.close();
            File file = new File(address + fileName);
            file.delete();
        }
        catch (SQLException e) {
            fail();
            System.out.println("Error closing database" + e.getMessage());
        }
    }
}
