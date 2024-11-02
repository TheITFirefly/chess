package dataaccess;

import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabaseUserDAOTest {

    private DatabaseUserDAO userDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        userDAO = new DatabaseUserDAO();
        userDAO.clearUsers(); // Clear the table before each test
    }

    @Test
    @Order(1)
    @DisplayName("Clear Users Positive")
    public void clearUsersPositive() throws DataAccessException {
        userDAO.createUser(new UserData("user1", "password1", "user1@example.com"));
        userDAO.clearUsers();
        assertNull(userDAO.getUser("user1"));
    }

    @Test
    @Order(2)
    @DisplayName("Create User Positive")
    public void createUserPositive() throws DataAccessException {
        UserData user = new UserData("user2", "password2", "user2@example.com");
        userDAO.createUser(user);
        UserData retrievedUser = userDAO.getUser("user2");
        assertNotNull(retrievedUser);
        assertEquals(user.username(), retrievedUser.username());
        assertEquals(user.password(), retrievedUser.password());
        assertEquals(user.email(), retrievedUser.email());
    }

    @Test
    @Order(3)
    @DisplayName("Create User Negative")
    public void createUserNegative() throws DataAccessException {
        UserData user = new UserData("user3", "password3", "user3@example.com");
        userDAO.createUser(user);
        DataAccessException thrown = assertThrows(DataAccessException.class, () -> {
            userDAO.createUser(new UserData("user3", "password4", "user4@example.com"));
        });
        assertTrue(thrown.getMessage().contains("Error inserting user"));
    }

    @Test
    @Order(4)
    @DisplayName("Get User Positive")
    public void getUserPositive() throws DataAccessException {
        UserData user = new UserData("user4", "password4", "user4@example.com");
        userDAO.createUser(user);
        UserData retrievedUser = userDAO.getUser("user4");
        assertNotNull(retrievedUser);
        assertEquals(user.username(), retrievedUser.username());
        assertEquals(user.password(), retrievedUser.password());
        assertEquals(user.email(), retrievedUser.email());
    }

    @Test
    @Order(5)
    @DisplayName("Get User Negative")
    public void getUserNegative() throws DataAccessException {
        UserData retrievedUser = userDAO.getUser("nonExistentUser");
        assertNull(retrievedUser);
    }
}
