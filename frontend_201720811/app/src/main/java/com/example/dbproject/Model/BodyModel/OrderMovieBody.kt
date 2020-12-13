package com.example.dbproject.Model.BodyModel

class OrderMovieBody(userId:String,orderId:String,movieRating:Float) {
    var customer_id :String = userId
    var order_id = orderId
    var rating = movieRating
    //var rating = movieRating
}