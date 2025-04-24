import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

// Base class Person


class Product {
    private String name;
    private String location;
    private int quantity;
    
    public Product(String name, String location, int quantity) {
        this.name = name;
        this.location = location;
        this.quantity = quantity;
    }
    
    public String getName() {
        return name;
    }
    
    public String getLocation() {
        return location;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}


abstract class Person {
    protected int id;
    protected String name;
    protected String surname;
    protected String login;
    protected String password;
    
    public Person(int id, String name, String surname, String login, String password) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
    }
    
    public int getId() {
        return id;
    }
    
    public String getLogin() {
        return login;
    }
    
    public String getPassword() {
        return password;
    }
    
    // Abstract method to demonstrate polymorphism
    public abstract void displayRole();
}

// Subclasses of Person
class Admin extends Person {
    public Admin(int id, String name, String surname, String login, String password) {
        super(id, name, surname, login, password);
    }
    
    @Override
    public void displayRole() {
        System.out.println("Role: Admin");
    }
}

class Manager extends Person {
    private String shift;
    
    public Manager(int id, String name, String surname, String login, String password, String shift) {
        super(id, name, surname, login, password);
        this.shift = shift;
    }
    
    public String getShift() {
        return shift;
    }
    
    @Override
    public void displayRole() {
        System.out.println("Role: Manager");
    }
}

class Worker extends Person {
    private String shift;
    
    public Worker(int id, String name, String surname, String login, String password, String shift) {
        super(id, name, surname, login, password);
        this.shift = shift;
    }
    
    public String getShift() {
        return shift;
    }
    
    @Override
    public void displayRole() {
        System.out.println("Role: Worker");
    }
}

// Shelf class
class Shelf {
    private int id;
    private String location;
    private Map<String, Integer> products;
    
    public Shelf(int id, String location) {
        this.id = id;
        this.location = location;
        this.products = new HashMap<>();
    }
    
    public void addProduct(String productId, int quantity) {
        products.put(productId, products.getOrDefault(productId, 0) + quantity);
    }
    
    public boolean reserveProduct(String productId, int quantity) {
        if (products.containsKey(productId) && products.get(productId) >= quantity) {
            products.put(productId, products.get(productId) - quantity);
            return true;
        }
        return false;
    }
    
    public String getLocation() {
        return location;
    }
    
    public Map<String, Integer> getProducts() {
        return products;
    }
}

class Order {
    private int id;
    private List<Product> products;
    private String status;
    
    public Order(int id, List<Product> products, String status) {
        this.id = id;
        this.products = products;
        this.status = status;
    }
    
    public int getId() {
        return id;
    }
    
    public List<Product> getProducts() {
        return products;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}


// Main Application Class
public class InventoryManagementApp {
    private static HashMap<String, Person> users = new HashMap<>();
    private static ArrayList<Order> orders = new ArrayList<>();
    private static ArrayList<Shelf> shelves = new ArrayList<>();
    private static JFrame frame;
    private static JPanel panel;
    private static JLabel label;
    private static Person currentUser;
    
    public static void main(String[] args) {
        // Example data setup
        
        Admin admin = new Admin(1, "John", "Doe", "admin1", "password123");
        Manager manager = new Manager(2, "Alice", "Smith", "manager1", "pass456", "Day");
        Worker worker1 = new Worker(3, "Bob", "Brown", "worker1", "pass789", "Night");
        Worker worker2 = new Worker(4, "Eve", "Davis", "worker2", "pass456", "Day");
        
        users.put(admin.getLogin(), admin);
        users.put(manager.getLogin(), manager);
        users.put(worker1.getLogin(), worker1);
        users.put(worker2.getLogin(), worker2);
        
        // Create example shelves
        Shelf shelf1 = new Shelf(1, "Aisle 1");
        Shelf shelf2 = new Shelf(2, "Aisle 2");
        shelf1.addProduct("P001", 10);
        shelf1.addProduct("P002", 5);
        shelf2.addProduct("P003", 8);
        shelves.add(shelf1);
        shelves.add(shelf2);
        
        // Initialize the UI
        frame = new JFrame("Inventory Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        label = new JLabel("Please log in:");
        label.setFont(new Font("Arial", Font.BOLD, 20)); // Increase font size
        panel.add(label);
        label.setForeground(Color.LIGHT_GRAY); // Ustawienie jasnoszarego koloru tekstu
        panel.setBackground(new Color(30,0,60));
        
        JTextField loginField = new JTextField(15);
        loginField.setFont(new Font("Arial", Font.PLAIN, 18));
        // Increase font size
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 18));
        // Increase font size
        JLabel loginLabel = new JLabel("Login:");
        loginLabel.setForeground(Color.LIGHT_GRAY); // Ustawienie jasnoszarego koloru tekstu
        panel.add(loginLabel);
        panel.add(loginField);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(new Color(192, 192, 192)); // Jasnoszary kolor (inny od Light Gray)
        panel.add(passwordLabel);
        panel.add(passwordField);
        
        panel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Wyśrodkowanie przycisków
        panel.setForeground(Color.LIGHT_GRAY); // Ustawienie jasnoszarego koloru tekstu
        
        // Przycisk "Pack Orders" w interfejsie
        JButton packOrdersButton = new JButton("Pack Orders");
        packOrdersButton.setFont(new Font("Arial", Font.BOLD, 18));
        packOrdersButton.setPreferredSize(new Dimension(200, 50));
        packOrdersButton.addActionListener(e -> openPackOrdersFrame());
        if (currentUserRole.equals("admin") || currentUserRole.equals("worker")) {
            panel.add(packOrdersButton); // Dodaj przycisk do panelu tylko dla admin i worker
        }
        
        
        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(300, 50)); // Set fixed size
        loginButton.setFont(new Font("Arial", Font.BOLD, 18)); // Larger font
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = loginField.getText();
                String password = new String(passwordField.getPassword());
                
                if (users.containsKey(login) && users.get(login).getPassword().equals(password)) {
                    currentUser = users.get(login);
                    JOptionPane.showMessageDialog(frame, "Welcome, " + currentUser.name + "!");
                    showMainMenu();
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid credentials. Try again.");
                }
            }
            
        });
        
        panel.add(loginButton);
        frame.add(panel);
        frame.setVisible(true);
    }
    
    private void openPackOrdersFrame() {
        JFrame packOrdersFrame = new JFrame("Pack Orders");
        packOrdersFrame.setSize(600, 400);
        packOrdersFrame.setLayout(new BorderLayout());
        packOrdersFrame.setLocationRelativeTo(null);
        
        // Pobierz listę zamówień ze statusem "Collected"
        java.util.List<Order> collectedOrders = getOrdersWithStatus("Collected");
        
        // Panel do wyboru zamówienia
        JPanel orderSelectionPanel = new JPanel();
        orderSelectionPanel.setLayout(new BoxLayout(orderSelectionPanel, BoxLayout.Y_AXIS));
        
        JLabel selectOrderLabel = new JLabel("Select an order to pack:");
        orderSelectionPanel.add(selectOrderLabel);
        
        // Lista rozwijana z zamówieniami
        JComboBox<Order> ordersComboBox = new JComboBox<>(collectedOrders.toArray(new Order[0]));
        orderSelectionPanel.add(ordersComboBox);
        
        // Panel do wprowadzania ilości produktów
        JPanel quantityPanel = new JPanel();
        quantityPanel.setLayout(new GridLayout(0, 2));
        
        JLabel quantityLabel = new JLabel("Enter quantities for products:");
        quantityPanel.add(quantityLabel);
        
        // Tworzenie pól do wpisania ilości dla każdego produktu w zamówieniu
        java.util.Map<Product, JTextField> quantityFields = new HashMap<>();
        JButton packButton = new JButton("Pack Order");
        
        packOrdersButton.addActionListener(e -> new InventoryManagementApp().openPackOrdersFrame());
        if (selectedOrder != null) {
            boolean isPacked = processPacking(selectedOrder, quantityFields);
            if (isPacked) {
                JOptionPane.showMessageDialog(packOrdersFrame, "Order packed successfully!");
                packOrdersFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(packOrdersFrame, "Packing failed. Quantities do not match!");
            }
        }
    });
    
    // Dodanie przycisku pakowania i ustawienia interfejsu
    packOrdersFrame.add(orderSelectionPanel, BorderLayout.NORTH);
    packOrdersFrame.add(quantityPanel, BorderLayout.CENTER);
    packOrdersFrame.add(packButton, BorderLayout.SOUTH);
    
    packOrdersFrame.setVisible(true);
}
private boolean processPacking(Order order, Map<Product, JTextField> quantityFields) {
    for (Product product : order.getProducts()) {
        int orderedQuantity = product.getQuantity();
        
        // Pobierz ilość wpisaną przez użytkownika
        JTextField field = quantityFields.get(product);
        int enteredQuantity = Integer.parseInt(field.getText());
        
        // Sprawdź zgodność ilości
        if (enteredQuantity != orderedQuantity) {
            return false; // Ilości nie zgadzają się
        }
    }
    
    // Jeśli wszystko się zgadza, zmień status zamówienia na "Packed"
    order.setStatus("Packed");
    return true;
}
private List<Order> getOrdersWithStatus(String status) {
    // Przyjmujemy, że istnieje klasa `Order` z metodą `getStatus()`
    return orders.stream()
    .filter(order -> order.getStatus().equalsIgnoreCase(status))
    .collect(Collectors.toList());
}


private static void showMainMenu() {
    panel.removeAll();  // Clear previous content
    panel.add(new JLabel("Main Menu"));
    
    JButton backButton = new JButton("Back");
    backButton.setEnabled(false); // Disable back button in main menu
    backButton.setPreferredSize(new Dimension(300, 50)); // Fixed size for consistency
    backButton.setFont(new Font("Arial", Font.BOLD, 18)); // Larger font
    
    panel.add(backButton);
    
    if (currentUser instanceof Admin) {
        showAdminMenu();
    } else if (currentUser instanceof Manager) {
        showManagerMenu();
    } else if (currentUser instanceof Worker) {
        showWorkerMenu();
    }
    
    frame.revalidate();
    frame.repaint();
}

private static void showAdminMenu() {
    panel.removeAll();  // Clear previous content
    panel.add(new JLabel("Admin Menu"));
    
    JButton viewWorkersButton = new JButton("View Workers");
    viewWorkersButton.setPreferredSize(new Dimension(300, 50));
    viewWorkersButton.setFont(new Font("Arial", Font.BOLD, 18)); // Larger font
    viewWorkersButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            displayAllWorkers();
        }
    });
    
    JButton viewOrdersButton = new JButton("View Orders");
    viewOrdersButton.setPreferredSize(new Dimension(300, 50));
    viewOrdersButton.setFont(new Font("Arial", Font.BOLD, 18)); // Larger font
    viewOrdersButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            displayAllOrders();
        }
    });
    
    JButton checkOrdersButton = new JButton("Check Orders");
    checkOrdersButton.setPreferredSize(new Dimension(300, 50));
    checkOrdersButton.setFont(new Font("Arial", Font.BOLD, 18)); // Larger font
    checkOrdersButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            checkOrders();
        }
    });
    
    JButton createAccountButton = new JButton("Create Account");
    createAccountButton.setPreferredSize(new Dimension(300, 50));
    createAccountButton.setFont(new Font("Arial", Font.BOLD, 18)); // Larger font
    createAccountButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showRegistrationForm(); // Show the registration form when clicked
        }
    });
    
    JButton backButton = new JButton("Back");
    backButton.setPreferredSize(new Dimension(300, 50)); // Fixed size for consistency
    backButton.setFont(new Font("Arial", Font.BOLD, 18)); // Larger font
    backButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showMainMenu(); // Navigate back to Main Menu
        }
    });
    
    panel.add(viewWorkersButton);
    panel.add(viewOrdersButton);
    panel.add(checkOrdersButton);
    panel.add(createAccountButton);
    panel.add(backButton);
    frame.revalidate();
    frame.repaint();
}

private static void checkOrders() {
    panel.removeAll();
    panel.add(new JLabel("--- Checking Orders ---"));
    
    for (Order order : orders) {
        JLabel orderLabel = new JLabel("Order ID: " + order.getId() + " - Status: " + order.getStatus());
        panel.add(orderLabel);
    }
    
    JButton backButton = new JButton("Back");
    backButton.setPreferredSize(new Dimension(300, 50));
    backButton.setFont(new Font("Arial", Font.BOLD, 18));
    backButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showAdminMenu();
        }
    });
    panel.add(backButton);
    frame.revalidate();
    frame.repaint();
}

private static void showManagerMenu() {
    panel.removeAll();  // Clear previous content
    panel.add(new JLabel("Manager Menu"));
    
    JButton viewOrdersButton = new JButton("View Orders");
    viewOrdersButton.setPreferredSize(new Dimension(300, 50));
    viewOrdersButton.setFont(new Font("Arial", Font.BOLD, 18));
    viewOrdersButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            displayAllOrders(); // Manager can view all orders
        }
    });
    
    JButton backButton = new JButton("Back");
    backButton.setPreferredSize(new Dimension(300, 50));
    backButton.setFont(new Font("Arial", Font.BOLD, 18));
    backButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showMainMenu(); // Navigate back to Main Menu
        }
    });
    
    panel.add(viewOrdersButton);
    panel.add(backButton);
    frame.revalidate();
    frame.repaint();
}

private static void showWorkerMenu() {
    panel.removeAll();  // Clear previous content
    panel.add(new JLabel("Worker Menu"));
    
    JButton collectOrderButton = new JButton("Collect Order");
    collectOrderButton.setPreferredSize(new Dimension(300, 50));
    collectOrderButton.setFont(new Font("Arial", Font.BOLD, 18)); // Larger font
    collectOrderButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            collectOrder(); // Start collecting the order
        }
    });
    
    JButton backButton = new JButton("Back");
    backButton.setPreferredSize(new Dimension(300, 50));
    backButton.setFont(new Font("Arial", Font.BOLD, 18)); // Larger font
    backButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showMainMenu(); // Navigate back to Main Menu
        }
    });
    
    panel.add(collectOrderButton);
    panel.add(backButton);
    frame.revalidate();
    frame.repaint();
}


private static void displayAllWorkers() {
    panel.removeAll();  // Clear previous content
    panel.add(new JLabel("All Workers:"));
    
    for (Person user : users.values()) {
        if (user instanceof Worker) {
            JLabel workerLabel = new JLabel("Worker: " + user.name + " " + user.surname);
            panel.add(workerLabel);
        }
    }
    
    JButton backButton = new JButton("Back");
    backButton.setPreferredSize(new Dimension(300, 50));
    backButton.setFont(new Font("Arial", Font.BOLD, 18));
    backButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showAdminMenu(); // Navigate back to Admin Menu
        }
    });
    
    panel.add(backButton);
    frame.revalidate();
    frame.repaint();
}

private static void displayAllOrders() {
    panel.removeAll();  // Clear previous content
    panel.add(new JLabel("All Orders:"));
    
    for (Order order : orders) {
        JLabel orderLabel = new JLabel("Order ID: " + order.getId() + " - Status: " + order.getStatus());
        panel.add(orderLabel);
    }
    
    JButton backButton = new JButton("Back");
    backButton.setPreferredSize(new Dimension(300, 50));
    backButton.setFont(new Font("Arial", Font.BOLD, 18));
    backButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showAdminMenu(); // Navigate back to Admin Menu
        }
    });
    
    panel.add(backButton);
    frame.revalidate();
    frame.repaint();
}

private static void showRegistrationForm() {
    panel.removeAll();  // Clear previous content
    panel.add(new JLabel("Registration Form"));
    
    JTextField nameField = new JTextField(15);
    JTextField surnameField = new JTextField(15);
    JTextField loginField = new JTextField(15);
    JPasswordField passwordField = new JPasswordField(15);
    
    panel.add(new JLabel("Name:"));
    panel.add(nameField);
    panel.add(new JLabel("Surname:"));
    panel.add(surnameField);
    panel.add(new JLabel("Login:"));
    panel.add(loginField);
    panel.add(new JLabel("Password:"));
    panel.add(passwordField);
    
    String[] roles = {"Admin", "Manager", "Worker"};
    JComboBox<String> roleComboBox = new JComboBox<>(roles);
    panel.add(new JLabel("Role:"));
    panel.add(roleComboBox);
    
    JButton submitButton = new JButton("Create Account");
    submitButton.setPreferredSize(new Dimension(300, 50));
    submitButton.setFont(new Font("Arial", Font.BOLD, 18)); // Larger font
    submitButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText();
            String surname = surnameField.getText();
            String login = loginField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();
            
            if (name.isEmpty() || surname.isEmpty() || login.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
                return;
            }
            
            int id = users.size() + 1; // Simple way to generate a new ID
            Person newUser = null;
            
            switch (role) {
                case "Admin":
                newUser = new Admin(id, name, surname, login, password);
                break;
                case "Manager":
                newUser = new Manager(id, name, surname, login, password, "Day");
                break;
                case "Worker":
                newUser = new Worker(id, name, surname, login, password, "Day");
                break;
            }
            
            if (newUser != null) {
                users.put(login, newUser);
                JOptionPane.showMessageDialog(frame, "Account created successfully!");
                showAdminMenu(); // Return to the Admin menu after creating account
            }
        }
    });
    
    JButton backButton = new JButton("Back");
    backButton.setPreferredSize(new Dimension(300, 50));
    backButton.setFont(new Font("Arial", Font.BOLD, 18));
    backButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showAdminMenu(); // Navigate back to Admin Menu
        }
    });
    
    panel.add(submitButton);
    panel.add(backButton);
    frame.revalidate();
    frame.repaint();
}
private static void collectOrder() {
    panel.removeAll();  // Clear previous content
    panel.add(new JLabel("Collecting Order"));
    
    Order order = createSampleOrder();  // Create a sample order for the worker to collect
    List<Product> products = order.getProducts();
    
    // Variables to keep track of the current product
    final int[] currentIndex = {0};
    
    // Display the first product
    displayProduct(products.get(currentIndex[0]), currentIndex, products);
}

private static void displayProduct(Product product, int[] currentIndex, List<Product> products) {
    panel.removeAll();  // Clear previous content
    panel.add(new JLabel("Collecting Product:"));
    
    // Display product information
    panel.add(new JLabel("Name: " + product.getName()));
    panel.add(new JLabel("Location: " + product.getLocation()));
    panel.add(new JLabel("Available Quantity: " + product.getQuantity()));
    
    JTextField quantityField = new JTextField(5);
    panel.add(new JLabel("Quantity to Collect:"));
    panel.add(quantityField);
    
    JButton submitButton = new JButton("Submit");
    submitButton.setPreferredSize(new Dimension(300, 50));
    submitButton.setFont(new Font("Arial", Font.BOLD, 18)); // Larger font
    submitButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int quantityCollected = Integer.parseInt(quantityField.getText());
            
            if (quantityCollected > product.getQuantity()) {
                JOptionPane.showMessageDialog(frame, "You cannot collect more than available.");
                return;
            }
            
            // Update the product quantity
            product.setQuantity(product.getQuantity() - quantityCollected);
            
            // If there are more products, display the next one
            if (currentIndex[0] < products.size() - 1) {
                currentIndex[0]++;
                displayProduct(products.get(currentIndex[0]), currentIndex, products);
            } else {
                JOptionPane.showMessageDialog(frame, "Order collection completed!");
                showWorkerMenu();  // Go back to worker menu after finishing the collection
            }
        }
    });
    
    JButton backButton = new JButton("Back");
    backButton.setPreferredSize(new Dimension(300, 50));
    backButton.setFont(new Font("Arial", Font.BOLD, 18)); // Larger font
    backButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showWorkerMenu();  // Navigate back to Worker Menu
        }
    });
    
    panel.add(submitButton);
    panel.add(backButton);
    frame.revalidate();
    frame.repaint();
}

private static Order createSampleOrder() {
    // Create a sample order with some products
    List<Product> products = List.of(
    new Product("Product 1", "Location A", 10),
    new Product("Product 2", "Location B", 5),
    new Product("Product 3", "Location C", 8)
    );
    
    // Provide an example id (e.g., 101) and status (e.g., "Being Collected")
    int orderId = 101;
    String status = "Being Collected";
    
    return new Order(orderId, products, status);  // Pass the required parameters
}
}