package net.cartola.cnpj.tolatlong;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 24/06/2020 00:09:13
 * @author murilotuvani
 */
public class ConnectionFactory {

    private Connection conn = null;
    private final String host;
    private final String database;
    private final String user;
    private final String passwd;
    
    public ConnectionFactory(CnpjLatLongConfig config) {
        this.host = config.getBdHost();
        this.database = config.getBdBancoDados();
        this.user = config.getBdUsuario();
        this.passwd = config.getBdSenha();
    }
    
    public Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database + "?useSSL=false", user, passwd);
        }
        return conn;
    }
    
    

}
