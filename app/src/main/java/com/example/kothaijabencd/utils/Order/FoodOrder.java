package com.example.kothaijabencd.utils.Order;

public class FoodOrder {
    String foodName, restaurantName, restaurantAddress, foodAmount, foodDetails, destination, order_date, delivery_date, delivery_by, order_type, order_by, current_location, user_token;

    public FoodOrder(String foodName, String restaurantName, String restaurantAddress, String foodAmount, String foodDetails, String destination, String order_date, String delivery_date, String delivery_by, String order_type, String order_by, String current_location, String user_token) {
        this.foodName = foodName;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.foodAmount = foodAmount;
        this.foodDetails = foodDetails;
        this.destination = destination;
        this.order_date = order_date;
        this.delivery_date = delivery_date;
        this.delivery_by = delivery_by;
        this.order_type = order_type;
        this.order_by = order_by;
        this.current_location = current_location;
        this.user_token = user_token;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public String getFoodAmount() {
        return foodAmount;
    }

    public void setFoodAmount(String foodAmount) {
        this.foodAmount = foodAmount;
    }

    public String getFoodDetails() {
        return foodDetails;
    }

    public void setFoodDetails(String foodDetails) {
        this.foodDetails = foodDetails;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getDelivery_by() {
        return delivery_by;
    }

    public void setDelivery_by(String delivery_by) {
        this.delivery_by = delivery_by;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getOrder_by() {
        return order_by;
    }

    public void setOrder_by(String order_by) {
        this.order_by = order_by;
    }

    public String getCurrent_location() {
        return current_location;
    }

    public void setCurrent_location(String current_location) {
        this.current_location = current_location;
    }

    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }

}
