import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Scanner;

public class UsersBase {
    private static Connection connection;
    private static Statement statement;

    public static void main(String[] args) {
        try {
            createDataSource();
            addUser();
            deletePerName();
            showUsersByAge(25, 40);


            try (ResultSet rs = statement.executeQuery("SELECT * FROM Users;")){
                while (rs.next()) {
                    int id = rs.getInt(1);
                    String name = rs.getString("name");
                    int age = rs.getInt("age");
                    String email = rs.getString("email");
                    System.out.println(id + " " + name + " " + age + " " + email);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }


    private static DataSource createDataSource() throws SQLException {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser("lucy");
        dataSource.setPassword("pass");
        dataSource.setUrl("jdbc:mysql://localhost:3306/users");
        dataSource.setServerTimezone("UTC");
        return dataSource;
    }


    public static void showUsersByAge(int min, int max) {
        System.out.println("Пользователи в возрасте от " + min + " до " + max +" :");

        try (ResultSet rs = statement.executeQuery("SELECT * FROM Users;")) {
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String email = rs.getString("email");
                if ((min <= age) && (age <= max)) {
                    System.out.println(id + " " + name + " " + age + " " + email);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void addUser() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/users")) {
            System.out.println("Введите имя нового пользователя: ");
            String name = scanner.nextLine();
            System.out.println("Введите возраст: ");
            int age = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Введите почту: ");
            String mail = scanner.nextLine();
            String add = "INSERT INTO users (name, age, email) Values (?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(add);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, mail);
            System.out.println("Пользователь " + name + " добавлен!");
            int rows = preparedStatement.executeUpdate();

        } catch (Exception ex) {
            System.out.println("Connection failed...");

            System.out.println(ex);
        }
    }

    public static void deletePerName() throws SQLException{
        Scanner scannerForDelete = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/users")) {
            System.out.println("Введите имя пользователя, которого необходимо удалить: ");
            String name = scannerForDelete.nextLine();
            String del = "DELETE FROM users WHERE Имя = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(del);
            preparedStatement.setString(1, name);
            System.out.println("Пользователь " + name + " успешно удален!");
            int rows = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}