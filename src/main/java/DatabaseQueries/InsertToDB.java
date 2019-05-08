/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseQueries;

import DatabaseQueries.CreateGame.createGameQuery;
import DatabaseQueries.CreateJugador.createJugadorQuery;
import DatabaseQueries.CreateJugadorEnPartida.createJugadorEnPartidaQuery;
import com.BitJunkies.RTS.src.Game;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Gibran Gonzalez
 */
/*
Hacer todas las queries de insertar:
    -Insertar Partida
    -Insertar Players
    -Insertar buildings
    -Insertar Units
*/
public class InsertToDB {
    // JDBC driver name and database URL
   static final String JDBC_DRIVER = "org.postgresql.Driver";  
   static final String DB_URL = "jdbc:postgresql://ec2-54-225-95-183.compute-1.amazonaws.com:5432/dah1sh2i3uomfn?user=seaynizasqgwhc&password=015554a88e5513b4c9011919b450cea41e4896ffdcc02c4880892b503b7b4020&sslmode=require";
   static final String connURL = "postgres://seaynizasqgwhc:015554a88e5513b4c9011919b450cea41e4896ffdcc02c4880892b503b7b4020@ec2-54-225-95-183.compute-1.amazonaws.com:5432/dah1sh2i3uomfn";
   static final String DATABASE_URL = "postgres://seaynizasqgwhc:015554a88e5513b4c9011919b450cea41e4896ffdcc02c4880892b503b7b4020@ec2-54-225-95-183.compute-1.amazonaws.com:5432/dah1sh2i3uomfn";
   //  Database credentials
   static final String USER = "seaynizasqgwhc";
   static final String PASS = "015554a88e5513b4c9011919b450cea41e4896ffdcc02c4880892b503b7b4020";
   
    /**
     * Method containing the SQL of the insertion of a row in a Player
     * @param username
     * @param password
     * @throws SQLException
     * @throws URISyntaxException
     */
    public static void insertPlayer(String username, String password) throws SQLException, URISyntaxException {
        String SQL = "INSERT INTO Jugador(username,password) "
                + "VALUES(?,?)";
        try (

                PreparedStatement statement = Game.conn.prepareStatement(SQL);) {
            
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
   }
   
    /**
     * Method containing the SQL of the insertions of the table PlayerInGame
     * @param list
     * @throws SQLException
     * @throws URISyntaxException
     */
    public static void insertJugadorEnPartida(ArrayList<CreateJugadorEnPartida.createJugadorEnPartidaQuery> list) throws SQLException, URISyntaxException {
        String SQL = "INSERT INTO JugadorEnPartida(jugadorId, partidaId, accionesPorMin, recursosAdquiridos, recursosConsumidos, edificiosconstruidos, unidadesConstruidas, isHost) "
                + "VALUES(?,?,?,?,?,?,?,?)";
        try (

                PreparedStatement statement = Game.conn.prepareStatement(SQL);) {
                int count = 0;

                for (createJugadorEnPartidaQuery q : list) {
                    statement.setString(1, q.jugadorId);
                    statement.setInt(2, q.partidaId);
                    statement.setInt(3, q.acciones);
                    statement.setInt(4, q.recursosAdquiridos);
                    statement.setInt(5, q.recursosConsumidos);
                    statement.setInt(6, q.edificiosConstruidos);
                    statement.setInt(7, q.unidadesConstruidas);
                    statement.setBoolean(8, q.host);


                    statement.addBatch();
                    count++;
                    // execute every 100 rows or less
                    if (count % 100 == 0 || count == list.size()) {
                        statement.executeBatch();
                    }
                }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
   
    /**
     * Method containing the SQL of the insertions of a row in the Game table
     * @param q
     * @return
     * @throws SQLException
     * @throws URISyntaxException
     */
    public static long insertGame(CreateGame.createGameQuery q) throws SQLException, URISyntaxException {
         q.idPartida = getCurrGameId()+1;
         System.out.println(q.idPartida);
         long id = 0;
         String SQL = "INSERT INTO Partida(id,inicio,termino,ganador) "
                + "VALUES(?,?,?,?)";
 
 
        try (
                PreparedStatement pstmt = Game.conn.prepareStatement(SQL,
                Statement.RETURN_GENERATED_KEYS)) {
 
            pstmt.setInt(1, q.idPartida);
            pstmt.setTimestamp(2, q.fechaIni);
            pstmt.setTimestamp(3, q.fechaFin);
            pstmt.setString(4, q.ganador);
 
            int affectedRows = pstmt.executeUpdate();
            // check the affected rows 
            if (affectedRows > 0) {
                // get the ID back
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        id = rs.getLong(1);
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return id;   
    }
        
    /**
     * Method containing the SQL for the query to get the current match
     * @return
     * @throws SQLException
     * @throws URISyntaxException
     */
    public static int getCurrGameId() throws SQLException, URISyntaxException {
        String SQL = "SELECT MAX(id) FROM Partida";
        int id=0;
 
        try (
                Statement stmt = Game.conn.createStatement();
                ResultSet rs = stmt.executeQuery(SQL)) {
            
            rs.next();
            id = rs.getInt(1);
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
        return id;
    }
}
