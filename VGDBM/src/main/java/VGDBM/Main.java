package VGDBM;

import java.util.Scanner;

/**
* The main class that represents the application.
* This class contains the main method that serves as the entry point for the application.
* Provides a command-line interface for managing a game database (with some bugs...).
*/
public class Main { 

    public static void main(String[] args) {
        
        DatabaseManager db = new DatabaseManager();
        String address = ".";
        String fileName = "games.db";

        try {
            db.open(address, fileName);
        } 
        
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        while (true) {
            
            System.out.println("The supported operations are:"
            + "\n[1] Add a new game"
            + "\n[2] Update game details"
            + "\n[3] Remove a game"
            + "\n[4] Save database to CSV file"
            + "\n[5] Close the database"
            + "\nWhat would you like to do? ");
        
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:

                    try {

                        System.out.println("Enter the name of the game");
                        String game = scanner.nextLine();

                        if (db.gameExists(game)) {
                            System.out.println("Game already in database");
                        }

                        else {
                            System.out.println("Enter the score");
                            String score = scanner.nextLine();
    
                            System.out.println("Enter the year");
                            String year = scanner.nextLine();

                            System.out.println("Enter the platform");
                            String platform = scanner.nextLine();

                            System.out.println("Enter the developer");
                            String developer = scanner.nextLine();

                            System.out.println("Enter the genre(s)");
                            String genre = scanner.nextLine();
    
                            System.out.println("Enter the notes");
                            String newnotes = scanner.nextLine();
                        
                            db.insertGame(new Game(game, score, year, platform, developer, genre, newnotes));
                        }

                        break;

                    }

                    catch (Exception e) {
                        System.out.println("Invalid input. Please enter information in the correct format.");
                        break;
                    }


                case 2:

                    try {

                        System.out.println("Enter the name of the game you want to update");
                        String gameName = scanner.nextLine();
                        if (!db.gameExists(gameName)) {
                            System.out.println("Game not found");
                            System.out.println(db.fetchGame(gameName).getGame());
                        }
                    
                        System.out.println("Enter the new name. Press enter to leave unchanged.");
                        String newGame = scanner.nextLine();
                        if (newGame.trim().equals("")) {
                            newGame = gameName;
                        }
                    
                        System.out.println("Enter the new score. Press enter to leave unchanged.");
                        String oldScore = db.fetchGame(gameName).getScore();
                        String newScore = scanner.nextLine();
                        if (newScore.trim().equals("") && oldScore == null) {
                            newScore = "-";
                        }
                        else if (newScore.trim().equals("")) {
                            newScore = oldScore;
                        }

                        System.out.println("Enter the new year. Press enter to leave unchanged.");
                        String oldYear = db.fetchGame(gameName).getYear();
                        String newYear = scanner.nextLine();
                        if (newYear.trim().equals("") && oldYear == null) {
                            newYear = "-";
                        }
                        else if (newYear.trim().equals("")) {
                            newYear = oldYear;
                        }

                        System.out.println("Enter the new platform. Press enter to leave unchanged.");
                        String oldPlatform = db.fetchGame(gameName).getPlatform();
                        String newPlatform = scanner.nextLine();
                        if (newPlatform.trim().equals("")) {
                            newPlatform = oldPlatform;
                        }

                        System.out.println("Enter the new developer. Press enter to leave unchanged.");
                        String newDeveloper = scanner.nextLine();
                        if (newDeveloper.trim().equals("")) {
                            newDeveloper = db.fetchGame(gameName).getDeveloper();
                        }

                        System.out.println("Enter the new genre(s). Press enter to leave unchanged.");
                        String newGenre = scanner.nextLine();
                        if (newGenre.trim().equals("")) {
                            newGenre = db.fetchGame(gameName).getGenre();
                        }

                        System.out.println("Enter the new notes. Press enter to leave unchanged.");
                        String newNotes = scanner.nextLine();
                        if (newNotes.trim().equals("")) {
                            newNotes = db.fetchGame(gameName).getNotes();
                        }
                    
                        db.updateGame(gameName, new Game(newGame, newScore, newYear, newPlatform, newDeveloper, newGenre, newNotes));

                    } 

                    catch (Exception e) {
                        System.out.println("Invalid input. Please enter information in the correct format.");
                        break;
                    }


                case 3:

                    System.out.println("Enter the name of the game you want to remove");
                    String gameToRemove = scanner.next();
                    try {
                        db.removeGame(gameToRemove);
                        break;
                    } 
                    catch (Exception e) {
                        System.out.println(e.getMessage());
                        break;
                    }


                case 4:
                    
                    try {
                        SQLiteToCSV factory = new SQLiteToCSV(db);
                        factory.writeCSV(address + "games.csv");
                        System.out.println("Database exported to games.csv");
                        break;
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        break;
                    }


                case 5:

                    try {
                        db.close();
                        System.out.println("Database closed.");
                        System.exit(0);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.exit(0);
                    }

                default:

                    System.out.println("Invalid choice");
            
            scanner.close();

            }
        }
    }
}
