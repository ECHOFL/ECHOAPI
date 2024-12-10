package me.fliqq.echosql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DbConnection {

    private DbCredentials dbCredentials;
    private Connection connection;

    public DbConnection(DbCredentials dbCredentials){
        this.dbCredentials = dbCredentials;
        this.connect();
    }

    private void connect(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            //cree la connection
            this.connection = DriverManager.getConnection(this.dbCredentials.toURL(), this.dbCredentials.getUser(), this.dbCredentials.getPass());
            Logger.getLogger("EchoPrison").info("Succesfully connected to DataBase");
        } catch (SQLException | ClassNotFoundException ex){
            ex.printStackTrace();
        }

    }
    public void close() throws SQLException {
        if(this.connection != null)
            if(!this.connection.isClosed())
                this.connection.close();
    }

    public Connection getConnection() throws SQLException {
        if(this.connection != null)
            if(!this.connection.isClosed())
                return this.connection;
        connect();
        return this.connection;
    }
}
