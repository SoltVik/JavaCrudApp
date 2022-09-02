import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

public class Database {
    List<Employee> employees = new ArrayList<>();
    Map<String, Employee> employeeMap = new HashMap<>();
    Map<String, Employee> sortedMap = new TreeMap<>();
    Set<String> positions = new HashSet<>();
    Mysql mysqlDB = new Mysql();
    private boolean testMode = false;

    Database(Collection<Employee> employees) {
        this.employees.addAll(employees);
        this.employees.forEach(e -> employeeMap.put(e.getName(), e));
    }

    Database() {
        this.employees.addAll(InitDb.getInitData());

        for (Employee employee : employees) {
            this.employeeMap.put(employee.getName(), employee);
            this.sortedMap.put(employee.getName(), employee);
            this.positions.add(employee.getPosition());
        }
    }

    public void create(Employee employee) {
        employees.add(employee);
        employeeMap.put(employee.getName(), employee);
        positions.add(employee.getPosition());
    }

    public void create(DefaultTableModel model, Employee employee) {
        employeeMap.put(employee.getName(), employee);
        positions.add(employee.getPosition());

        model.addRow(
                new Object[]{
                        employee.getName(),
                        employee.getPosition(),
                        String.valueOf(employee.getSalary()),
                        String.valueOf(employee.getAge())
                }
        );

        Thread thread = new Thread(() -> mysqlDB.insert(employee.getName(), employee.getPosition(), employee.getSalary(), employee.getAge()));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Employee> read() {
        return employees;
    }

    public Employee find(String name) {
        return employeeMap.get(name);
    }

    public boolean update (String name, Employee employee) {
        if (delete(name, false)) {
            create(employee);
            return true;
        }
        return false;
    }

    public void update(DefaultTableModel model, int row, String name, Employee employee) {
        delete(name, false);
        create(employee);

        model.setValueAt(employee.getName(), row, 0);
        model.setValueAt(employee.getPosition(), row, 1);
        model.setValueAt(String.valueOf(employee.getSalary()), row, 2);
        model.setValueAt(String.valueOf(employee.getAge()), row, 3);

        Thread thread = new Thread(() -> mysqlDB.update(name, employee.getName(), employee.getPosition(), employee.getSalary(), employee.getAge()));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean delete(String name, boolean delFromDB) {
        Employee employee = employeeMap.get(name);
        if (employee != null) {
            employeeMap.remove(name);
            employees.remove(employee);

            if (delFromDB) {
                Thread thread = new Thread(() -> mysqlDB.delete(name));
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        return false;
    }

    public String checkInsertData(String name, String position, String salary, String age) {
        if ((Objects.equals(name, "")) || (Objects.equals(position, "")) || (Objects.equals(salary, "")) || (Objects.equals(age, ""))) {
            return "emptyFields";
        } else if (employeeMap.get(name) != null) {
            return "alreadyExist";
        }
        return "null";
    }

    public String checkInsertData(String name, String oldName, String position, String salary, String age) {
        if ((Objects.equals(name, "")) || (Objects.equals(position, "")) || (Objects.equals(salary, "")) || (Objects.equals(age, ""))) {
            return "emptyFields";
        } else if ((employeeMap.get(name) != null) && (!name.equals(oldName))) {
            return "alreadyExist";
        }
        return "null";
    }


    public boolean export(String path) {
        ExportToCSV export = new ExportToCSV();
        return export.export(path, employees);
    }

    public void setTestMode(boolean mode) {
        testMode = mode;
    }

    public String[] getPositions() {
        return employees.stream()
                .map(e -> e.getPosition())
                .distinct()
                .toArray(String[]::new);
    }
}