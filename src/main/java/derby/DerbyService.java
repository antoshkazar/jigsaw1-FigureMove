package derby;

import dto.GameResult;

import java.sql.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Класс работы с БД.
 */
public class DerbyService {
    public static final String GET_RESULTS = "select * from results order by END_DATE, TIME_SPENT, STEPS" +
            " fetch first 10 rows only";

    /**
     * Вызывается, когда пользователь запрашивает топ игр.
     * @return возвращаем список лучших игр, отсортированный по нужным параметрам.
     */
    public static List<GameResult> getResultsTable() {
        try (final Connection connection = DerbyConnectionProvider.getConnection()) {
            List<GameResult> results = new ArrayList<>();
            ResultSet resultSet = connection.prepareStatement(GET_RESULTS).executeQuery();
            while (resultSet.next()) {
                GameResult gameResult = new GameResult();
                gameResult.setLogin(resultSet.getString(2));
                gameResult.setEndTime(resultSet.getTimestamp(3).toInstant());
                gameResult.setTotalSteps(resultSet.getInt(4));
                gameResult.setTimeSpent(resultSet.getLong(5));
                results.add(gameResult);
            }
            return results;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Записываем результаты игры текущего юзера в БД.
     * @param login Логин пользователя.
     * @param endTime Время окончания им игры (т.е. запроса сохранения)
     * @param startTime Время начала игры.
     * @param steps Количество размещенных фигур.
     */
    public static void writeGameResults(String login, Instant endTime, Instant startTime, int steps) {
        try (final Connection connection = DerbyConnectionProvider.getConnection()) {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("insert into RESULTS (ID, LOGIN, END_DATE, STEPS, TIME_SPENT) " +
                            "VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setString(1, UUID.randomUUID().toString());
            preparedStatement.setString(2, login);
            preparedStatement.setTimestamp(3, Timestamp.from(endTime));
            preparedStatement.setInt(4, steps);
            preparedStatement.setLong(5, Duration.between(startTime, endTime).getSeconds());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
