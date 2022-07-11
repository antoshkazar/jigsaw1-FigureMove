package derby;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Класс, устанавливающий соединение с БД.
 */
public class DerbyConnectionProvider {

    public static final String CREATE_TABLE = "CREATE TABLE results (" +
            "id varchar(36) primary key," +
            "login varchar(255)," +
            "end_date timestamp," +
            "steps int," +
            "time_spent bigint" +
            ")";

    /**
     * Попытка установления связи с БД (для записи и считывания, вызывается из класса DerbyService)
     * @return подключение к БД
     */
    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:derby:jigsaw;create=true");
        // Выводим в консоль данные о подключении.
        System.out.println(connection);
        try {
            connection.prepareStatement(CREATE_TABLE).executeUpdate();
        } catch (SQLException e) {
            if (!e.getMessage().contains("'RESULTS' already exists")) {
                throw e;
            }
        }
        return connection;
    }
}
