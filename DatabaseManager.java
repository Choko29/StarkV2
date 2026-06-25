import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/app_db";
    private static final String USER = "root";
    private static final String PASSWORD = ""; 

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void addProduct(Product product) throws SQLException {
        String query = "INSERT INTO app_table (item_name, item_value1, item_value2) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getValue1()); // 🌟 შეიცვალა: სეტავს როგორც String-ს (setDouble-ის ნაცვლად)
            stmt.setInt(3, product.getValue2());
            stmt.executeUpdate();
        }
    }

    public static List<Product> getAllProducts() throws SQLException {
        List<Product> list = new ArrayList<>();
        String query = "SELECT * FROM app_table";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("id"),
                        rs.getString("item_name"),
                        rs.getString("item_value1"), // 🌟 შეიცვალა: კითხულობს როგორც String-ს (getDouble-ის ნაცვლად)
                        rs.getInt("item_value2")
                );
                list.add(product);
            }
        }
        return list;
    }
}
