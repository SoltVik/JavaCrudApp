/**
 * Java. MiniProject #1
 *
 * @author Viktors Soltums
 * @version 02 Sep 2022
 */
/*
 Информационная CRUD система, позволяющая работать со списком сотрудников.
 На базе кода занятия №10.
 Создаётся maven-проект.
 Применяем: collections, stream, io, nio, exceptions, junit5, maven.
 Данные о сотрудниках хранятся в текстовом файле (*в базе данных).
 */

import javax.swing.JFrame;

public class ApplicationCRUD {

    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(() -> {
            JFrame f = new JFrame("Employees Table");
            f.getContentPane().add(new EmployeesTable().getComponent());
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(800, 600);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });

    }
}