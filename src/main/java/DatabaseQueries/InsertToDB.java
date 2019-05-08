/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseQueries;

import DatabaseQueries.CreateGame.createGameQuery;
import DatabaseQueries.CreateJugador.createJugadorQuery;
import DatabaseQueries.CreateJugadorEnPartida.createJugadorEnPartidaQuery;
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
   
   //  Database credentials
   static final String USER = "seaynizasqgwhc";
   static final String PASS = "015554a88e5513b4c9011919b450cea41e4896ffdcc02c4880892b503b7b4020";
   
   public Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }
   
   public void insertPlayers(ArrayList<CreateJugador.createJugadorQuery> list) {
        String SQL = "INSERT INTO Jugador(username,password) "
                + "VALUES(?,?)";
        try (
                Connection conn = connect();
                PreparedStatement statement = conn.prepareStatement(SQL);) {
            int count = 0;
 
            for (createJugadorQuery q : list) {
                statement.setString(1, q.username);
                statement.setString(2, q.password);
 
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
   
   public void insertGame() {
       
   }
   
   public void insertJugadorEnPartida(ArrayList<CreateJugadorEnPartida.createJugadorEnPartidaQuery> list) {
        String SQL = "INSERT INTO JugadorEnPartida(jugadorId, partidaId, accionesPorMin, recursosAdquiridos, edificiosconstruidos, unidadesConstruidas, hostea) "
                + "VALUES(?,?,?,?,?,?,?)";
        try (
                Connection conn = connect();
                PreparedStatement statement = conn.prepareStatement(SQL);) {
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
   
    public long insertGame(CreateGame.createGameQuery q) {
         q.idPartida = getCurrGameId();
         long id = 0;
         String SQL = "INSERT INTO Partida(id,inicio,fin,ganador) "
                + "VALUES(?,?)";
 
 
        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(SQL,
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
        
    public int getCurrGameId() {
        String SQL = "SELECT MAX(id) FROM Partida";
        int id=0;
 
        try (Connection conn = connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(SQL)) {
            
            rs.next();
            id = rs.getInt(1);
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
        return id;
    }
}
