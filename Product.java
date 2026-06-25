public class Product {
    private int id;
    private String name;      // ტექსტური ველი (პროდუქტის სახელი, წიგნის სათაური და ა.შ.)
    private double value1;    // რიცხვითი ველი 1 (ფასი, წონა, რეიტინგი)
    private int value2;       // რიცხვითი ველი 2 (რაოდენობა, გვერდები, წელი)

    // კონსტრუქტორი ბაზიდან მონაცემების წასაკითხად
    public Product(int id, String name, double value1, int value2) {
        this.id = id;
        this.name = name;
        this.value1 = value1;
        this.value2 = value2;
    }

    // კონსტრუქტორი ბაზაში ახალი მონაცემის ჩასაწერად
    public Product(String name, double value1, int value2) {
        this.name = name;
        this.value1 = value1;
        this.value2 = value2;
    }

    // გეთერები (Getters) - აუცილებელია Stream API-სთვის
    public int getId() { return id; }
    public String getName() { return name; }
    public double getValue1() { return value1; }
    public int getValue2() { return value2; }
}
