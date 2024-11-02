package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public class DatabaseAuthDAO implements AuthDAO {
    @Override
    public void clearAuths() throws DataAccessException {
        try {
            initializeAuthTable();
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed during table initialization: " + e.getMessage());
        }
        try (var conn = DatabaseManager.getConnection()) {
            try (var stmt = conn.prepareStatement("DELETE FROM authdata;")) {
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error dropping authdata table: " + e.getMessage());
        }
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        try {
            initializeAuthTable();
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed during table initialization: " + e.getMessage());
        }
        try (var conn = DatabaseManager.getConnection()) {
            try (var insertStmt = conn.prepareStatement("INSERT INTO authdata (authtoken, username) VALUES (?, ?)")) {
                insertStmt.setString(1, authData.authToken());
                insertStmt.setString(2, authData.username());
                insertStmt.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException("Error inserting auth data into authdata table: " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error obtaining connection: " + e.getMessage());
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try {
            initializeAuthTable();
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed during table initialization: " + e.getMessage());
        }
        try (var conn = DatabaseManager.getConnection()) {
            try (var stmt = conn.prepareStatement("SELECT * FROM authdata WHERE authtoken = ?")) {
                stmt.setString(1, authToken);
                try (var rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return new AuthData(rs.getString("authtoken"), rs.getString("username"));
                    }
                }
            } catch (SQLException e) {
                throw new DataAccessException("Error selecting auth from authdata table: " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error obtaining connection: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        try {
            initializeAuthTable();
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed during table initialization: " + e.getMessage());
        }
        try (var conn = DatabaseManager.getConnection()) {
            try (var deleteStmt = conn.prepareStatement("DELETE FROM authdata WHERE authtoken = ?")) {
                deleteStmt.setString(1, authToken);
                deleteStmt.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException("Error deleting auth from authdata table: " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error obtaining connection: " + e.getMessage());
        }
    }

    private void initializeAuthTable() throws DataAccessException {
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed during database creation: " + e.getMessage());
        }
        try (var conn = DatabaseManager.getConnection()) {
            try (var stmt = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS userdata (" +
                            "username VARCHAR(255) PRIMARY KEY, " +
                            "password VARCHAR(255) NOT NULL, " +
                            "email VARCHAR(255) NOT NULL);")) {
                stmt.executeUpdate();
            }
            try (var stmt = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS authdata (" +
                            "authtoken VARCHAR(255) PRIMARY KEY, " +
                            "username VARCHAR(255) NOT NULL, " +
                            "FOREIGN KEY (username) REFERENCES userdata(username) " +
                            "ON DELETE CASCADE);")) {
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error initializing auth table: " + e.getMessage());
        }
    }
}
