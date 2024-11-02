package dataaccess;

import dataaccess.DatabaseAuthDAO;
import dataaccess.DatabaseUserDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabaseAuthDAOTest {

    private DatabaseUserDAO userDAO;
    private DatabaseAuthDAO authDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        userDAO = new DatabaseUserDAO();
        authDAO = new DatabaseAuthDAO();
        userDAO.clearUsers();
        authDAO.clearAuths();
        userDAO.createUser(new UserData("user1", "password1", "user1@example.com"));
    }

    @Test
    @Order(1)
    @DisplayName("Clear Auths Positive")
    public void clearAuthsPositive() throws DataAccessException {
        authDAO.createAuth(new AuthData("token1", "user1"));
        authDAO.clearAuths();
        assertNull(authDAO.getAuth("token1"));
    }

    @Test
    @Order(2)
    @DisplayName("Create Auth Positive")
    public void createAuthPositive() throws DataAccessException {
        // Use existing user "user1" from setUp
        AuthData auth = new AuthData("token2", "user1");
        authDAO.createAuth(auth);

        AuthData retrievedAuth = authDAO.getAuth("token2");
        assertNotNull(retrievedAuth);
        assertEquals(auth.authToken(), retrievedAuth.authToken());
        assertEquals(auth.username(), retrievedAuth.username());
    }

    @Test
    @Order(3)
    @DisplayName("Create Auth Negative")
    public void createAuthNegative() throws DataAccessException {
        AuthData auth = new AuthData("token3", "user1");
        authDAO.createAuth(auth);
        DataAccessException thrown = assertThrows(DataAccessException.class, () -> {
            authDAO.createAuth(new AuthData("token3", "user1")); // Attempt with duplicate token
        });
        assertTrue(thrown.getMessage().contains("Error inserting auth data"));
    }

    @Test
    @Order(4)
    @DisplayName("Get Auth Positive")
    public void getAuthPositive() throws DataAccessException {
        AuthData auth = new AuthData("token4", "user1");
        authDAO.createAuth(auth);
        AuthData retrievedAuth = authDAO.getAuth("token4");
        assertNotNull(retrievedAuth);
        assertEquals(auth.authToken(), retrievedAuth.authToken());
        assertEquals(auth.username(), retrievedAuth.username());
    }

    @Test
    @Order(5)
    @DisplayName("Get Auth Negative")
    public void getAuthNegative() throws DataAccessException {
        AuthData retrievedAuth = authDAO.getAuth("nonExistentToken");
        assertNull(retrievedAuth);
    }

    @Test
    @Order(6)
    @DisplayName("Delete Auth Positive")
    public void deleteAuthPositive() throws DataAccessException {
        AuthData auth = new AuthData("token5", "user1");
        authDAO.createAuth(auth);
        authDAO.deleteAuth("token5");
        assertNull(authDAO.getAuth("token5"));
    }

    @Test
    @Order(7)
    @DisplayName("Delete Auth Negative")
    public void deleteAuthNegative() throws DataAccessException {
        assertDoesNotThrow(() -> authDAO.deleteAuth("nonExistentToken"));
    }
}
