package com.example.dbproject.Model

import com.google.gson.annotations.SerializedName

class OrderModel {

    class OrderInfo{
        @SerializedName("item")
        var orderItem = OrderItem()
    }

    class OrderItem(){
        @SerializedName("order_id")
        var orderId = 0
        @SerializedName("movie_id")
        var movieId = 0
        @SerializedName("customer_id")
        var customerId = ""
        @SerializedName("created_time")
        var createdTime = ""
        @SerializedName("return_date")
        var returnDate = ""
        @SerializedName("is_returned")
        var checkReturn = 0
    }
}