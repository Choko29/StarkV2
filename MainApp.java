import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainApp extends Application {

    private PieChart pieChart;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("უნივერსალური საგამოცდო აპლიკაცია (app_db)");

        // --- 1. ვიზუალური ელემენტების შექმნა ---
        Label labelName = new Label("სახელი / სათაური:");
        TextField fieldName = new TextField();

        Label labelVal1 = new Label("მონაცემი 1 (მაგ. ფასი):");
        TextField fieldVal1 = new TextField();

        Label labelVal2 = new Label("მონაცემი 2 (მაგ. რაოდენობა):");
        TextField fieldVal2 = new TextField();

        Button btnAdd = new Button("მონაცემთა ბაზაში დამატება");

        // --- 2. კომპონენტების განლაგება GridPane-ში ---
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));

        grid.add(labelName, 0, 0);
        grid.add(fieldName, 1, 0);
        grid.add(labelVal1, 0, 1);
        grid.add(fieldVal1, 1, 1);
        grid.add(labelVal2, 0, 2);
        grid.add(fieldVal2, 1, 2);
        grid.add(btnAdd, 1, 3);

        // --- 3. ჩარტის (PieChart) ინიციალიზაცია ---
        pieChart = new PieChart();
        pieChart.setTitle("სტატისტიკური განაწილება (Stream API)");

        // --- 4. დამატების ღილაკის ფუნქციონალი ---
        btnAdd.setOnAction(e -> {
            try {
                String name = fieldName.getText();
                double v1 = Double.parseDouble(fieldVal1.getText());
                int v2 = Integer.parseInt(fieldVal2.getText());

                Product p = new Product(name, v1, v2);
                DatabaseManager.addProduct(p);

                fieldName.clear();
                fieldVal1.clear();
                fieldVal2.clear();

                // ჩარტის მომენტალური განახლება
                updatePieChart();

                showNotification(Alert.AlertType.INFORMATION, "წარმატება", "მონაცემი წარმატებით დაემატა ბაზაში!");
            } catch (Exception ex) {
                showNotification(Alert.AlertType.ERROR, "შეცდომა", "გთხოვთ შეიყვანოთ სწორი ფორმატის მონაცემები!");
            }
        });

        // აპლიკაციის პირველივე ჩართვისას ჩარტის შევსება ბაზაში არსებული მონაცემებით
        updatePieChart();

        // --- 5. მთავარი კონტეინერის აწყობა და ჩვენება ---
        VBox root = new VBox(20, grid, pieChart);
        root.setPadding(new Insets(15));

        Scene scene = new Scene(root, 480, 650);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // --- 🌟 ლექტორის მოთხოვნა: JAVA STREAM API 🌟 ---
    private void updatePieChart() {
        try {
            // 1. წამოვიღოთ ყველა პროდუქტი სიის სახით ბაზიდან
            List<Product> products = DatabaseManager.getAllProducts();

            // 2. Stream API-თ დაჯგუფება სახელით და რაოდენობების (Value2) შეჯამება
            Map<String, Integer> groupedData = products.stream()
                    .collect(Collectors.groupingBy(
                            Product::getName,                         // დაჯგუფების კრიტერიუმი (Key)
                            Collectors.summingInt(Product::getValue2) // რისი შეჯამებაც ხდება (Value)
                    ));

            // 3. ძველი ჩარტის მონაცემების გასუფთავება
            pieChart.getData().clear();

            // 4. ახალი დაჯგუფებული მონაცემების დასმა ჩარტზე
            groupedData.forEach((name, totalQty) -> {
                String chartLabel = name + " - " + totalQty + " ცალი";
                pieChart.getData().add(new PieChart.Data(chartLabel, totalQty));
            });

        } catch (Exception ex) {
            System.out.println("ჩარტის განახლების შეცდომა: " + ex.getMessage());
        }
    }

    private void showNotification(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
