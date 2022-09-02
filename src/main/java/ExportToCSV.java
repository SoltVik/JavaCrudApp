import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ExportToCSV {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    Calendar calendar = Calendar.getInstance();
    String shortDate = formatter.format(calendar.getTime());

    private final String EXPORT_FILE_NAME = shortDate + "_employees.csv";
    private static final String FILE_SEPARATOR = "\\";
    private static final String HEADER = "Name,Position,Salary,Age";
    private static final String COMMA_DELIMITER = ",";
    private static final String LINE_SEPARATOR = "\n";

    public boolean export(String path, List<Employee> employees) {
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(path + FILE_SEPARATOR + EXPORT_FILE_NAME);

            fileWriter.append(HEADER);
            fileWriter.append(LINE_SEPARATOR);

            for (Employee e : employees) {
                fileWriter.append(e.getName());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(e.getPosition());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(e.getSalary()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(e.getAge()));
                fileWriter.append(LINE_SEPARATOR);
            }
            return true;
        } catch (Exception ee) {
            ee.printStackTrace();
        } finally {
            try {
                fileWriter.close();
            } catch (IOException ie) {
                ie.printStackTrace();
            }
        }
        return false;
    }
}
