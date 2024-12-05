package ws;

import java.util.Objects;

public class SessionKey {
    public enum Role {
        BLACK, WHITE, OBSERVER
    }

    private final Role role;
    private final Integer gameID;
    private final String username;

    public SessionKey(Role role, Integer gameID, String username) {
        this.role = role;
        this.gameID = gameID;
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public Integer getGameID() {
        return gameID;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (!(o instanceof SessionKey)) {return false;}
        SessionKey that = (SessionKey) o;
        return role == that.role &&
                Objects.equals(gameID, that.gameID) &&
                Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(role, gameID, username);
    }

    @Override
    public String toString() {
        return "SessionKey{" +
                "role=" + role +
                ", gameID=" + gameID +
                ", username='" + username + '\'' +
                '}';
    }
}
