import java.sql.*;

public class Database {
    private Connection connection;
    private Statement statement;

    // Constructor koneksi ke MySQL
    public Database() {
        try {
            // Sesuaikan URL, username, dan password database Anda
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/db_people",
                    "root",
                    ""
            );
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException("Koneksi Database Gagal: " + e.getMessage());
        }
    }

    // Digunakan untuk SELECT
    public ResultSet selectQuery(String sql) {
        try {
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Digunakan untuk INSERT, UPDATE, DELETE
    public int insertUpdateDeleteQuery(String sql) {
        try {
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Statement getStatement() {
        return statement;
    }
}