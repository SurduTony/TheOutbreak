package other;

import java.sql.*;
import Entities.*;
import Main.*;

public class SaveManager {
    Game game;
    Handler handler;
    Soldier soldier;

    public SaveManager(Game game, Handler handler) {
        this.game = game;
        this.handler = handler;
        this.soldier = handler.getSoldier();
    }

    public void save() {
        System.out.println("Save");

        String sql;
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:save.db");
            stmt = c.createStatement();

            sql = "drop table if exists SAVE";
            stmt.execute(sql);

            sql = "CREATE TABLE SAVE " +
                    "(Difficulty INT NOT NULL," +
                    " Level INT NOT NULL," +
                    " Score INT NOT NULL, " +
                    " Deaths INT NOT NULL)";
            stmt.execute(sql);

            // insert
            sql = "INSERT INTO SAVE (Difficulty, Level, Score, Deaths) " +
                    "VALUES ("+game.difficulty+", "+game.levelIndex+", "+soldier.totalKills+", "+soldier.nrDeaths+");";
            stmt.executeUpdate(sql);

            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }

    public void load() {
        System.out.println("Load");

        String sql;
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:save.db");
            stmt = c.createStatement();

            // get values from database
            ResultSet rs = stmt.executeQuery("SELECT * FROM SAVE;");
            while (rs.next()) {
                game.difficulty = rs.getInt("Difficulty");
                game.levelIndex = rs.getInt("Level");
                soldier.totalKills = rs.getInt("Score");
                soldier.nrDeaths = rs.getInt("Deaths");
            }
            rs.close();

            game.levelLoader.load(game.levelIndex);
            game.inMenu = false;

            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }
}
