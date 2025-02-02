import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) {
        Connection conn = DBConnection.connect();
        if (conn != null) {
            System.out.println("Database Connected Successfully!");
        } else {
            System.out.println("Connection Failed! Check error messages.");
        }
    }

    void setVisible(boolean b) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}