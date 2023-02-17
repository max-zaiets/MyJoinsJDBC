package org.example;
import java.beans.Statement;
import java.sql.*;
public class Main {
    public static void main(String[] args) {
        String URL = "jdbc:mysql://localhost:3306/myjoinsjdbcdb";
        String LOGIN = "root";
        String PASSWORD = "root";

        registerDriver();

        Connection connection = null;
        PreparedStatement statement = null;

        System.out.println("\n Получить контактные данные сотрудников (номера телефонов, место жительства)");
        try {
            connection= DriverManager.getConnection(URL, LOGIN, PASSWORD);
            statement = connection.prepareStatement("SELECT staff.name, staff.phone, personalInfo.adress\n" +
                    "FROM staff\n" +
                    "JOIN personalInfo\n" +
                    "ON staff.id = personalInfo.staff_id;");
            viewTable(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                connection.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        System.out.println("\n Получите информацию о дате рождения всех холостых сотрудников и их номера \n" +
                "В данной бд холостых 3 человека \t");
        try {
            connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
            statement = connection.prepareStatement("SELECT staff.name, personalInfo.birth_day, staff.phone\n" +
                    "FROM staff\n" +
                    "JOIN personalInfo\n" +
                    "ON staff.id = personalInfo.staff_id\n" +
                    "WHERE personalInfo.maritalStatus IN ('не женат', 'не замужем');");
            viewTable(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        System.out.println("\n Получите информацию обо всех менеджерах компании: дату рождения и номер телефона \n" +
                "В данной бд холостых 3 человека \t");
        try {
            connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
            statement = connection.prepareStatement("SELECT staff.name, personalInfo.birth_day, staff.phone\n" +
                    "FROM staff\n" +
                    "JOIN personalInfo\n" +
                    "ON staff.id = personalInfo.staff_id\n" +
                    "JOIN serviceInfo\n" +
                    "ON staff.id = serviceInfo.staff_id\n" +
                    "WHERE serviceInfo.position = 'Менеджер';");
            viewTable(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }

    public static void registerDriver(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver successfully registered");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void viewTable(PreparedStatement statement) throws SQLException {
            ResultSet result = statement.executeQuery();
            ResultSetMetaData rsm = result.getMetaData();
        for (int i = 1; i <= rsm.getColumnCount(); i++)
            System.out.print(rsm.getColumnName(i) + "\t\t\t");
        System.out.println();
        while (result.next()) {
            for (int j = 1; j <= rsm.getColumnCount(); j++) {
                System.out.print(result.getString(j) + "\t\t");
            }
            System.out.println();
        }

    }
}