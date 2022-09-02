import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class Mysql {
    private static final String DATABASE_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/java";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private Connection connection;
    private Properties properties;

    private Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            properties.setProperty("user", USERNAME);
            properties.setProperty("password", PASSWORD);
        }
        return properties;
    }

    public Connection connect() {
        if (connection == null) {
            try {
                Class.forName(DATABASE_DRIVER);
                connection = DriverManager.getConnection(DATABASE_URL, getProperties());
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }


    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void insert(String name, String position, int salary, int age) {
        Mysql mysqlDB = new Mysql();
        String sql = "INSERT INTO `employees` (name, position, salary, age) VALUES ('" + name + "', '" + position + "', '" + salary + "', '" + age + "')";

        try {
            PreparedStatement statement = mysqlDB.connect().prepareStatement(sql);
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mysqlDB.disconnect();
        }
    }

    public void delete(String name) {
        Mysql mysqlDB = new Mysql();
        String sql = "DELETE FROM `employees` WHERE name = '" + name + "'";

        try {
            PreparedStatement statement = mysqlDB.connect().prepareStatement(sql);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mysqlDB.disconnect();
        }
    }

    public void update(String updateName, String name, String position, int salary, int age) {
        Mysql mysqlDB = new Mysql();
        String sql = "UPDATE `employees` SET name = '" + name + "', position = '" + position + "', salary = '" + salary + "', age = '" + age + "' WHERE name = '" + updateName + "'";

        try {
            PreparedStatement statement = mysqlDB.connect().prepareStatement(sql);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mysqlDB.disconnect();
        }
    }
}
