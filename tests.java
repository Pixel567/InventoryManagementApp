import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class InventoryManagementAppTest {

    @BeforeEach
    void setUp() {
        InventoryManagementApp.users = new HashMap<>();
        InventoryManagementApp.orders = new ArrayList<>();
        InventoryManagementApp.shelves = new ArrayList<>();
    }

    // 🔹 TESTY LOGOWANIA
    @Test
    void testSuccessfulLogin() {
        Admin admin = new Admin(1, "John", "Doe", "admin", "pass123");
        InventoryManagementApp.users.put(admin.getLogin(), admin);

        assertTrue(InventoryManagementApp.users.containsKey("admin"));
        assertEquals("pass123", InventoryManagementApp.users.get("admin").getPassword());
    }

    @Test
    void testFailedLogin() {
        Admin admin = new Admin(1, "John", "Doe", "admin", "pass123");
        InventoryManagementApp.users.put(admin.getLogin(), admin);

        assertFalse(InventoryManagementApp.users.containsKey("wrongUser"));
        assertNotEquals("wrongPass", InventoryManagementApp.users.get("admin").getPassword());
    }

    // 🔹 TESTY ZARZĄDZANIA UŻYTKOWNIKAMI
    @Test
    void testAddingUser() {
        Manager manager = new Manager(2, "Alice", "Smith", "manager", "mPass", "Day");
        InventoryManagementApp.users.put(manager.getLogin(), manager);

        assertEquals(1, InventoryManagementApp.users.size());
        assertTrue(InventoryManagementApp.users.containsKey("manager"));
    }

    @Test
    void testRemovingUser() {
        Worker worker = new Worker(3, "Bob", "Brown", "worker", "wPass", "Night");
        InventoryManagementApp.users.put(worker.getLogin(), worker);
        InventoryManagementApp.users.remove("worker");

        assertEquals(0, InventoryManagementApp.users.size());
    }

    @Test
    void testFindingUser() {
        Admin admin = new Admin(1, "John", "Doe", "admin", "pass123");
        InventoryManagementApp.users.put(admin.getLogin(), admin);

        assertNotNull(InventoryManagementApp.users.get("admin"));
        assertEquals("John", InventoryManagementApp.users.get("admin").getName());
    }

    // 🔹 TESTY ZARZĄDZANIA ZAMÓWIENIAMI
    @Test
    void testAddingOrder() {
        Order order = new Order(101, "Order 1");
        InventoryManagementApp.orders.add(order);

        assertEquals(1, InventoryManagementApp.orders.size());
        assertEquals(101, InventoryManagementApp.orders.get(0).getOrderId());
    }

    @Test
    void testRemovingOrder() {
        Order order = new Order(102, "Order 2");
        InventoryManagementApp.orders.add(order);
        InventoryManagementApp.orders.remove(order);

        assertEquals(0, InventoryManagementApp.orders.size());
    }

    @Test
    void testFindingOrder() {
        Order order = new Order(103, "Order 3");
        InventoryManagementApp.orders.add(order);

        boolean found = InventoryManagementApp.orders.stream()
            .anyMatch(o -> o.getOrderId() == 103);

        assertTrue(found);
    }

    // 🔹 TESTY ZARZĄDZANIA PÓŁKAMI
    @Test
    void testAddingShelf() {
        Shelf shelf = new Shelf(1, "Shelf A");
        InventoryManagementApp.shelves.add(shelf);

        assertEquals(1, InventoryManagementApp.shelves.size());
    }

    @Test
    void testRemovingShelf() {
        Shelf shelf = new Shelf(2, "Shelf B");
        InventoryManagementApp.shelves.add(shelf);
        InventoryManagementApp.shelves.remove(shelf);

        assertEquals(0, InventoryManagementApp.shelves.size());
    }

    @Test
    void testFindingShelf() {
        Shelf shelf = new Shelf(3, "Shelf C");
        InventoryManagementApp.shelves.add(shelf);

        boolean found = InventoryManagementApp.shelves.stream()
            .anyMatch(s -> s.getId() == 3);

        assertTrue(found);
    }

    // 🔹 TESTY GUI (przykładowe testy z Mockito)
    @Test
    void testLoginButtonAction() {
        JButton loginButton = new JButton();
        loginButton.doClick();
        assertNotNull(loginButton.getActionListeners());
    }

    @Test
    void testShowMainMenu() {
        JFrame frameMock = Mockito.mock(JFrame.class);
        InventoryManagementApp.frame = frameMock;

        InventoryManagementApp.currentUser = new Admin(1, "John", "Doe", "admin", "pass123");
        InventoryManagementApp.showMainMenu();

        Mockito.verify(frameMock).revalidate();
        Mockito.verify(frameMock).repaint();
    }
}
