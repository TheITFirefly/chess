package dataaccess;

import model.UserData;

import java.sql.SQLException;

public class DatabaseUserDAO implements UserDAO {

    @Override
    public void clearUsers() throws DataAccessException {
        try {
            initializeUserTable();
        } catch (DataAccessException e) {
            throw  new DataAccessException("Failed during table initialization: "+e.getMessage());
        }
        try (var conn = DatabaseManager.getConnection()) {
            try (var stmt = conn.prepareStatement(
                    "DELETE FROM userdata;")) {
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error clearing userdata table: " + e.getMessage());
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try {
            initializeUserTable();
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
        try (var conn = DatabaseManager.getConnection()) {
            try (var stmt = conn.prepareStatement("SELECT * FROM userdata WHERE username = ?")) {
                stmt.setString(1, username);
                try (var rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return new UserData(rs.getString("username"), rs.getString("password"),rs.getString("email"));
                    }
                }
            } catch (SQLException e) {
                throw new DataAccessException("Error selecting user from userdata table: " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error selecting user from userdata table: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        try {
            initializeUserTable();
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
        try (var conn = DatabaseManager.getConnection()) {
            try (var insertStmt = conn.prepareStatement("INSERT INTO userdata (username, password, email) VALUES (?, ?, ?)")) {
                insertStmt.setString(1, userData.username());
                insertStmt.setString(2, userData.password());
                insertStmt.setString(3, userData.email());

                insertStmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error inserting user into userdata table: " + e.getMessage());
        }
    }

    private void initializeUserTable() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var stmt = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS userdata (" +
                            "username VARCHAR(255) PRIMARY KEY, " +
                            "password VARCHAR(255) NOT NULL, " +
                            "email VARCHAR(255) NOT NULL);")) {
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error initializing user table: " + e.getMessage());
        }
    }
}
