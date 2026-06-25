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
        primaryStage.setTitle("საგამოცდო აპლიკაცია (უნივერსალური ვერსია)");

        Label labelName = new Label("პროდუქტის სახელი:");
        TextField fieldName = new TextField();

        Label labelVal1 = new Label("მონაცემი 1 (ფასი, კატეგორია ან თარიღი):");
        TextField fieldVal1 = new TextField();

        Label labelVal2 = new Label("რაოდენობა (მხოლოდ მთელი რიცხვი):");
        TextField fieldVal2 = new TextField();

        Button btnAdd = new Button("ბაზაში დამატება");
        
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(15));
        grid.add(labelName, 0, 0); grid.add(fieldName, 1, 0);
        grid.add(labelVal1, 0, 1); grid.add(fieldVal1, 1, 1);
        grid.add(labelVal2, 0, 2); grid.add(fieldVal2, 1, 2);
        grid.add(btnAdd, 1, 3);

        pieChart = new PieChart();
        pieChart.setTitle("პროდუქტების განაწილება რაოდენობის მიხედვით");

        btnAdd.setOnAction(e -> {
            try {
                String name = fieldName.getText();
                String v1 = fieldVal1.getText(); // 🌟 შეიცვალა: პირდაპირ იღებს ტექსტს ყოველგვარი Parse-ის გარეშე!
                
                // მეორე მონაცემი აუცილებლად გადაგვაქვს რიცხვში ჩარტის მათემატიკისთვის
                int v2 = Integer.parseInt(fieldVal2.getText());

                Product p = new Product(name, v1, v2);
                DatabaseManager.addProduct(p);

                fieldName.clear();
                fieldVal1.clear();
                fieldVal2.clear();

                updatePieChart();

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "მონაცემი წარმატებით დაემატა!");
                alert.showAndWait();
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "რაოდენობის ველში შეიყვანეთ მხოლოდ მთელი რიცხვი!");
                alert.showAndWait();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "ბაზასთან კავშირი ვერ დამყარდა!");
                alert.showAndWait();
            }
        });

        updatePieChart();

        VBox root = new VBox(20, grid, pieChart);
        root.setPadding(new Insets(15));
        primaryStage.setScene(new Scene(root, 480, 650));
        primaryStage.show();
    }

    // 🌟 ეს მეთოდი 100%-ით ასრულებს ლექტორის პირობას: აჯამებს რეალურ რაოდენობებს
    private void updatePieChart() {
        try {
            List<Product> products = DatabaseManager.getAllProducts();

            Map<String, Integer> groupedData = products.stream()
                    .collect(Collectors.groupingBy(
                            Product::getName,
                            Collectors.summingInt(Product::getValue2) 
                    ));

            pieChart.getData().clear();
            
            groupedData.forEach((name, totalQty) -> {
                String chartLabel = name + " - " + totalQty + " ცალი";
                pieChart.getData().add(new PieChart.Data(chartLabel, totalQty));
            });

        } catch (Exception ex) {
            System.out.println("ჩარტის შეცდომა: " + ex.getMessage());
        }
    }

    public static void main(String[] args) { launch(args); }
}
