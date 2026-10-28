package com.grocery;

public class Grocery {


private int id;
private String item;
private int qty;
private double price;
private boolean brgt;

public Grocery() {
}

public Grocery(int id, String item, int qty,
               double price, boolean brgt) {
    this.id = id;
    this.item = item;
    this.qty = qty;
    this.price = price;
    this.brgt = brgt;
}

public int getId() {
    return id;
}

public void setId(int id) {
    this.id = id;
}

public String getItem() {
    return item;
}

public void setItem(String item) {
    this.item = item;
}

public int getQty() {
    return qty;
}

public void setQty(int qty) {
    this.qty = qty;
}

public double getPrice() {
    return price;
}

public void setPrice(double price) {
    this.price = price;
}

public boolean isBought() {
    return brgt;
}

public void setBought(boolean bought) {
    this.brgt = bought;
}

public double getTotal() {
    return qty * price;
}


}
