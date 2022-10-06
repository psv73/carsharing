package carsharing;

import java.sql.*;

public class DatabaseAccess {

    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:./src/carsharing/db/carsharing";

    static final String USER = "sa";
    static final String PASS = "";

    Connection connection;
    Statement statement;

    public void createConnection() {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(true);
            statement = connection.createStatement();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeQuery() {
        try {
            if(statement != null) statement.close();
            if(connection != null) connection.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public ResultSet executeQuery(String str) {
        ResultSet rs = null;
        try {
            rs = statement.executeQuery(str);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return rs;
    }

    public void executeUpdate(String sql) {
        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);
            conn.setAutoCommit(true);
            stmt = conn.createStatement();

            stmt.executeUpdate(sql);

            stmt.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public void createTables() {
        /*
         *  create table Company
         */
        String sql = """
                CREATE TABLE IF NOT EXISTS COMPANY (
                        id INTEGER PRIMARY KEY AUTO_INCREMENT,  
                        name VARCHAR(255) NOT NULL UNIQUE
                    );
                   """;

        executeUpdate(sql);

        /*
         * create table Car
         */
        sql = """
                CREATE TABLE IF NOT EXISTS Car (
                        id INTEGER PRIMARY KEY AUTO_INCREMENT,  
                        name VARCHAR(255) NOT NULL UNIQUE,
                        company_id INTEGER NOT NULL,
                        CONSTRAINT fk_company FOREIGN KEY (company_id)
                        REFERENCES Company(id) 
                    );
                   """;

        executeUpdate(sql);

        sql = """
                CREATE TABLE IF NOT EXISTS Customer (
                    id INTEGER PRIMARY KEY AUTO_INCREMENT, 
                    name VARCHAR(50) UNIQUE NOT NULL, 
                    rented_car_id INTEGER DEFAULT NULL,  
                    CONSTRAINT fk_car FOREIGN KEY (rented_car_id) 
                    REFERENCES Car(id)
                );
                """;

        executeUpdate(sql);
    }

    public void clearDatabase() {
        String sql = "DROP TABLE IF EXISTS Customer";

        executeUpdate(sql);

        sql = "DROP TABLE IF EXISTS Car";

        executeUpdate(sql);

        sql = "DROP TABLE IF EXISTS Company";

        executeUpdate(sql);

        createTables();

        sql = "ALTER TABLE company ALTER COLUMN id RESTART WITH 1";
        executeUpdate(sql);

        System.out.println("Tables was cleaned.");
        System.out.println();
    }
}
