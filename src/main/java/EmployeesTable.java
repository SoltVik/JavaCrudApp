import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

public class EmployeesTable extends JFrame {
    private JTable table;
    private JButton addButton, updateButton, deleteButton, findButton, exportButton;
    private JTextField nameText, positionText, salaryText, ageText, findText;
    private DefaultTableModel model;
    private String[] columns;
    private JPanel mainPanel = new JPanel(new BorderLayout());
    private JFileChooser fileChooser = new JFileChooser();
    private Database database = new Database();

    public EmployeesTable() {
        columns = new String[]{"Name", "Position", "Salary", "Age"};

        List<Employee> employeeList;
        employeeList = database.employees;

        model = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Employee e : employeeList) {
            Object[] o = new Object[4];
            o[0] = e.getName();
            o[1] = e.getPosition();
            o[2] = Integer.toString(e.getSalary());
            o[3] = Integer.toString(e.getAge());
            model.addRow(o);
        }

        table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        findButton = new JButton("Find");
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        exportButton = new JButton("Export");

        JPanel buttonPanel = new JPanel();

        findText = new JTextField(10);
        buttonPanel.add(new JLabel("Find name"));
        buttonPanel.add(findText);
        buttonPanel.add(findButton);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(exportButton);


        //Select row
        ListSelectionListener listener;
        table.getSelectionModel().addListSelectionListener(listener = e -> {
            int i = table.getSelectedRow();
            nameText.setText((String) model.getValueAt(i, 0));
            positionText.setText((String) model.getValueAt(i, 1));
            salaryText.setText((String) model.getValueAt(i, 2));
            ageText.setText((String) model.getValueAt(i, 3));
        });

        //Add button
        addButton.addActionListener(e -> {
            String name = nameText.getText();
            String position = positionText.getText();
            String salary = salaryText.getText();
            String age = ageText.getText();
            String error = database.checkInsertData(name, position, salary, age);

            switch (error) {
                case ("emptyFields"):
                    showMessage("Not all fields are filled", "Message", JOptionPane.ERROR_MESSAGE);
                    break;
                case ("alreadyExist"):
                    showMessage(name + " already exist", "Message", JOptionPane.ERROR_MESSAGE);
                    break;
                default:
                    database.create(model, new Employee(name, position, Integer.parseInt(salary), Integer.parseInt(age)));
                    nameText.setText("");
                    positionText.setText("");
                    salaryText.setText("");
                    ageText.setText("");
                    break;
            }
        });

        // Update button
        updateButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row > -1) {
                String name = nameText.getText();
                String position = positionText.getText();
                String salary = salaryText.getText();
                String age = ageText.getText();
                String oldName = (String) table.getValueAt(row, 0);

                String error = database.checkInsertData(name, oldName, position, salary, age);

                switch (error) {
                    case ("emptyFields"):
                        showMessage("Not all fields are filled", "Message", JOptionPane.ERROR_MESSAGE);
                        break;
                    case ("alreadyExist"):
                        showMessage(name + " already exist", "Message", JOptionPane.ERROR_MESSAGE);
                        break;
                    default:
                        database.update(model, row, oldName, new Employee(name, position, Integer.parseInt(salary), Integer.parseInt(age)));
                        showMessage("Updated successfully", "Message", JOptionPane.INFORMATION_MESSAGE);
                        break;
                }
            }
        });

        //Delete button
        deleteButton.addActionListener(ae -> {
            table.getSelectionModel().removeListSelectionListener(listener);

            if (table.getSelectedRow() != -1) {
                String name = (String) table.getValueAt(table.getSelectedRow(), 0);
                int confirm = JOptionPane.showConfirmDialog(null, "Do you want to remove " + name + "?", "Deleting " + name, JOptionPane.YES_NO_OPTION);

                if (confirm == 0) {
                    model.removeRow(table.getSelectedRow());
                    database.delete(name, true);
                    nameText.setText("");
                    positionText.setText("");
                    salaryText.setText("");
                    ageText.setText("");
                    showMessage("Deleted successfully", "Message", JOptionPane.INFORMATION_MESSAGE);
                }
            }

            table.getSelectionModel().addListSelectionListener(listener);
        });

        // Find button
        findButton.addActionListener(e -> {
            table.getSelectionModel().removeListSelectionListener(listener);
            TableRowSorter<TableModel> sort = new TableRowSorter<>(table.getModel());
            table.setRowSorter(sort);

            String str = findText.getText();
            if (str.trim().length() == 0) {
                sort.setRowFilter(null);
                showMessage("Filter reset successfully ", "Message", JOptionPane.INFORMATION_MESSAGE);
            } else {
                sort.setRowFilter(RowFilter.regexFilter("(?i)" + str, 0));
                showMessage("Sorted successfully ", "Message", JOptionPane.INFORMATION_MESSAGE);
            }
            table.getSelectionModel().addListSelectionListener(listener);
        });

        exportButton.addActionListener(e -> {
            fileChooser.setDialogTitle("Saving Data");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showSaveDialog(EmployeesTable.this);
            String path = fileChooser.getSelectedFile().getPath();

            if (result == JFileChooser.APPROVE_OPTION) {
                boolean export = database.export(path);
                if (export) {
                    showMessage("Database exported successfully ", "Message", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    showMessage("Error while exporting file ", "Message", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        JPanel textPanel = new JPanel(new BorderLayout());
        nameText = new JTextField(10);
        positionText = new JTextField(10);
        salaryText = new JTextField(10);
        ageText = new JTextField(10);

        textPanel.setLayout(new GridLayout(4, 2));
        textPanel.add(new JLabel("Name"));
        textPanel.add(nameText);
        textPanel.add(new JLabel("Position"));
        textPanel.add(positionText);
        textPanel.add(new JLabel("Salary"));
        textPanel.add(salaryText);
        textPanel.add(new JLabel("Age"));
        textPanel.add(ageText);

        mainPanel.add(textPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }


    public JComponent getComponent() {
        return mainPanel;
    }

    public void showMessage(String msg, String title, int msgType) {
        JOptionPane.showMessageDialog(null, msg, title, msgType);
    }

}