/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbexamples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author ahozyainov
 */
public class DataBase implements AutoCloseable {

    static Connection connection;
    static PreparedStatement delete;
    static PreparedStatement insert;
    static PreparedStatement select;
    static String title;
    static String issued;
    static String rating;
    static String director;
    static String name;
    static String lastname;

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws SQLException, IOException {

        System.out.println("delete or add item?");
        System.out.print("-> ");

        switch (choose().toLowerCase()) {
            case "delete":
                delete();
                break;
            case "add":
                insert();
                break;
            default:
                break;
        }
    }

    //метод для записи выбора в диалоге
    private static String choose() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String choose = reader.readLine();
        return choose;
    }

    //метод диалога добавления данных
    private static void insert() throws SQLException, IOException {

        System.out.print("add director or movie? --> ");

        switch (choose().toLowerCase()) {
            case "director":
                System.out.print("input name -> ");
                name = choose();
                System.out.print("input lastname -> ");
                lastname = choose();
                //добавляем режиссера
                addDirector(name, lastname);
                break;
            case "movie":
                System.out.print("input title --> ");
                title = choose();
                System.out.print("input issued --> ");
                issued = choose();
                System.out.print("input rating --> ");
                rating = choose();
                System.out.print("input director lastname --> ");
                lastname = choose();
                //добавляем фильм
                addMovie(title, issued, rating, lastname);
                break;
        }

    }

    //метод диалога удаления данных
    private static void delete() throws SQLException, IOException {

        System.out.print("delete director or movie? --> ");

        switch (choose().toLowerCase()) {
            case "director":
                System.out.print("input director's lastname --> ");
                lastname = choose();
                //удаляем режиссера
                deleteDirector(lastname);
                break;
            case "movie":
                System.out.print("input movie -->  ");
                title = choose();
                //удаляем фильм
                deleteMovie(title);
                break;

        }

    }

    //метод добавления режисера
    private static void addDirector(String name, String lastname) throws SQLException {
        connect();
        try {
            insert = connection.prepareStatement("INSERT Into DIRECTORS (name, lastname) values (?, ?)");
            insert.setString(1, name);
            insert.setString(2, lastname);
            System.out.println(checkQuery(insert.executeUpdate()));
            insert.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    //метод добавления фильма - здесь не удалось через .setString сделать...
    private static void addMovie(String title, String issued, String rating, String lastname) throws SQLException {
        connect();
        try {
            String sql = "INSERT Into MOVIES (title, issued, rating, director) values (" + "'" + title + "'" + ", " + "'" + issued + "'" + ", (select id \n"
                    + "            from ratings \n"
                    + "            where name = '16+'), (select id \n"
                    + "            from directors \n"
                    + "            where \n"
                    + "                lastname = 'won'))";
            insert = connection.prepareStatement(sql);
            System.out.println(checkQuery(insert.executeUpdate()));
            insert.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //метод удаления фильма
    private static void deleteMovie(String title) throws SQLException {
        connect();
        try {
            delete = connection.prepareStatement(
                    "DELETE From MOVIES Where TITLE = " + "'" + title + "'");
            System.out.println(checkQuery(delete.executeUpdate()));
            delete.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    //метод удаления режиссера
    private static void deleteDirector(String lastname) throws SQLException {
        connect();
        try {
            delete = connection.prepareStatement(
                    "DELETE From DIRECTORS Where LASTNAME = " + "'" + lastname + "'");

            System.out.println(checkQuery(delete.executeUpdate()));
            delete.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    //подключение к DB
    private static void connect() throws SQLException {
        try {
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/video-store", "manager", "root");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    //метод проверки выполнения запроса
    private static String checkQuery(int x) {
        String str;
        if (x == 1) {
            str = "Well done!";
        } else {
            str = "Failure...";
        }
        return str;

    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
