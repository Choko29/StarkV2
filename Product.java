public class Product {
    private int id;
    private String name;
    private String value1; // 🌟 შეიცვალა: გახდა String (double-ის ნაცვლად)
    private int value2;    // დარჩა მთელი რიცხვი (int)

    public Product(int id, String name, String value1, int value2) {
        this.id = id;
        this.name = name;
        this.value1 = value1;
        this.value2 = value2;
    }

    public Product(String name, String value1, int value2) {
        this.name = name;
        this.value1 = value1;
        this.value2 = value2;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getValue1() { return value1; } // 🌟 შეიცვალა: აბრუნებს String-ს
    public int getValue2() { return value2; }
}
