package dto;

import java.io.Serializable;
import java.time.Instant;

public class GameResult implements Serializable {
    // Логин пользователя.
    private String login;
    // Время окончания игры.
    private Instant endTime;
    // Количество размещенных пользователем фигур.
    private int totalSteps;
    // В секундах.
    private long timeSpent;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
    }

    public long getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(long timeSpent) {
        this.timeSpent = timeSpent;
    }
}
