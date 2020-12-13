package com.example.dbproject.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class MovieList{
    @SerializedName("list")
    var movieList = ArrayList<Movie>()
}

class MovieInfo{
    @SerializedName("item")
    var movieItem = MovieItem()
}

class MovieItem{
    @SerializedName("movie_id")
    var movieId = 0
    @SerializedName("movie_name")
    var movieName = ""
    @SerializedName("movie_type")
    var movieType = ""
    @SerializedName("num_of_copies")
    var copies = -1
    @SerializedName("rating")
    var movieRating = -1
    @SerializedName("actor")
   var actorList = ArrayList<ActorModel>()
}

class Movie(name:String="", type:String="",numOfCopies:Int=-1,rating:Int=-1) {

    @Expose
    @SerializedName("order_id")
    var orderId = 0
    @Expose
    @SerializedName("is_expired")
    var checkExpired = 0
    @Expose
    @SerializedName("customer_id")
    var customerId = ""
    @Expose
    @SerializedName("created_time")
    var createdTime = ""
    @Expose
    @SerializedName("return_date")
    var returnDate = ""
    @Expose
    @SerializedName("is_returned")
    var checkReturn = 0

    @SerializedName("movie_id")
    var movieId = 0
    @SerializedName("movie_name")
    var movieName = name
    @SerializedName("movie_type")
    var movieType = type
    @SerializedName("num_of_copies")
    var copies = numOfCopies
    @SerializedName("rating")
    var movieRating = rating

}