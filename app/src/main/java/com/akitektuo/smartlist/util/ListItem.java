package com.akitektuo.smartlist.util;

/**
 * Created by AoD Akitektuo on 14-Mar-17.
 */

public class ListItem {

    private int number;
    private String value;
    private String currency;
    private String product;
    private int buttonType;

    public ListItem(int number, String value, String currency, String product, int buttonType) {
        setNumber(number);
        setValue(value);
        setCurrency(currency);
        setProduct(product);
        setButtonType(buttonType);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getButtonType() {
        return buttonType;
    }

    public void setButtonType(int buttonType) {
        this.buttonType = buttonType;
    }

}
