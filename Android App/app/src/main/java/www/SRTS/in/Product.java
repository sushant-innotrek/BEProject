package www.SRTS.in;

import java.util.PriorityQueue;

public class Product {
    private String name;
    private Double netWt;
    private Double packagedWt;
    private Double price;
    private Long count;
    private String key;

    public Product(String name, Double netWt, Double packagedWt, Double price, Long count,String key) {
        this.name = name;
        this.netWt = netWt;
        this.packagedWt = packagedWt;
        this.price = price;
        this.count = count;
        this.key = key;
    }

    Product(){
        count= Long.valueOf(1);

    }
    public String getName() {
        return name;
    }

    public Double getNetWt() {
        return netWt;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Double getPackagedWt() {
        return packagedWt;
    }

    public Double getPrice() {
        return price;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNetWt(Double netWt) {
        this.netWt = netWt;
    }

    public void setPackagedWt(Double packagedWt) {
        this.packagedWt = packagedWt;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
