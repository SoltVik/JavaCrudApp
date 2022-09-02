import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestDatabase {
    private Database db;

    @BeforeEach
    public void init() {
        db = new Database(List.of(new Employee("Joy", "assistant", 750, 25)));
        db.setTestMode(true);
    }

    @Test
    public void testCreate() {
        List<Employee> employees;

        Employee employee = new Employee("John", "director", 1250, 45);
        db.create(employee);

        employees = db.read();
        Assertions.assertEquals(employees.size(), 2);

        Employee found = db.find("John");
        Assertions.assertNotNull(found);
    }

    @Test
    public void testRead() {
        List<Employee> employees = db.read();
        boolean read = employees.equals(db.read());
        Assertions.assertTrue(read);
    }

    @Test
    public void testUpdate() {
        boolean result = db.update("Joy", new Employee("Mark", "engineer", 1050, 42));
        Assertions.assertTrue(result);

        Employee employee = db.find("Joy");
        Assertions.assertNull(employee);

        employee = db.find("Mark");
        Assertions.assertNotNull(employee);
    }

    @Test
    public void testDelete() {
        boolean result = db.delete("Joy", false);
        Assertions.assertTrue(result);

        Employee employee = db.find("Joy");
        Assertions.assertNull(employee);

        List<Employee> employees = db.read();
        Assertions.assertEquals(employees.size(), 0);
    }
}
