package com.example.dbproject.Model.BodyModel

class LikeMovieBody(userId: String, movieId :String, likeWeight:Int) {
    var customer_id :String = userId
    var movie_id = movieId
    var weight = likeWeight
}