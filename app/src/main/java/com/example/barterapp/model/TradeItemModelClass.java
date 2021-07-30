package com.example.barterapp.model;

import java.io.Serializable;

public class TradeItemModelClass implements Serializable {

    private String itemId;
    private String name;
    private String imageUrl;
    private String traderID;
    private String description;
    private String category;
    private String tradeWith;
    private String condition;
    private int quantity;
    private  Double totalStars=0.0;
    private int total_reviews=0;

    public TradeItemModelClass(){

    }

    public TradeItemModelClass(String itemId, String name, String imageUrl, String traderID, String description, String category, String tradeWith, String condition, int quantity) {
        this.itemId = itemId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.traderID = traderID;
        this.description = description;
        this.category = category;
        this.tradeWith = tradeWith;
        this.condition = condition;
        this.quantity = quantity;
    }

    public TradeItemModelClass(String name, String imageUrl, String traderID, String description) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.traderID = traderID;
        this.description = description;
    }

    public TradeItemModelClass(String name, String imageUrl, String traderID, String description, String category, String tradeWith, String condition, int quantity) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.traderID = traderID;
        this.description = description;
        this.category = category;
        this.tradeWith = tradeWith;
        this.condition = condition;
        this.quantity = quantity;
    }

    public Double getTotalStars() {
        return totalStars;
    }

    public void setTotalStars(Double totalStars) {
        this.totalStars = totalStars;
    }

    public int getTotal_reviews() {
        return total_reviews;
    }

    public void setTotal_reviews(int total_reviews) {
        this.total_reviews = total_reviews;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTraderID() {
        return traderID;
    }

    public void setTraderID(String traderID) {
        this.traderID = traderID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTradeWith() {
        return tradeWith;
    }

    public void setTradeWith(String tradeWith) {
        this.tradeWith = tradeWith;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
