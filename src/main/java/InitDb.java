import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class InitDb {
    private static final ArrayList<Employee> initData = new ArrayList<>();

    public static ArrayList<Employee> getInitData() {

        Mysql mysqlDB = new Mysql();
        String sql = "SELECT * FROM `employees` ORDER BY `name` ASC";
        ResultSet result;
        try {
            PreparedStatement statement = mysqlDB.connect().prepareStatement(sql);

            result = statement.executeQuery();
            while (result.next()) {
                String name = result.getString("name");
                String position = result.getString("position");
                int salary = result.getInt("salary");
                int age = result.getInt("age");
                initData.add(new Employee(name, position, salary, age));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mysqlDB.disconnect();
        }
        return initData;
    }
}