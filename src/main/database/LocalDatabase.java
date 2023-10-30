package database;

import models.Authtoken;
import models.Game;
import models.User;

import java.util.Vector;

public class LocalDatabase
{
    private static Vector<Authtoken> authtokenList = new Vector<>();
    private static Vector<Game> gameList = new Vector<>();
    private static Vector<User> userList = new Vector<>();
    private static int gameID = 1;

    public static int getGameID() {
        return gameID++;
    }

    public static void setGameID(int gameID) {
        LocalDatabase.gameID = gameID;
    }

    public static Vector<Authtoken> getAuthtokenList() { return authtokenList; }
    public static void addAuthtoken(Authtoken authtoken) { authtokenList.add(authtoken); }
    public static void clearAuthtokenList() { authtokenList = new Vector<>(); }

    public static Vector<Game> getGameList() { return gameList; }
    public static void addGame(Game game) { gameList.add(game); }
    public static void clearGameList() { gameList = new Vector<>(); }

    public static Vector<User> getUserList() { return userList; }
    public static void addUser(User user) { userList.add(user); }
    public static void clearUserList() { userList = new Vector<>(); }
}
