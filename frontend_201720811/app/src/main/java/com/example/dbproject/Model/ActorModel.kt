package com.example.dbproject.Model

import com.google.gson.annotations.SerializedName

class ActorModel(name:String, sex:String, age:Int, rating: Double) {
    @SerializedName("actor_id")
    var actorId = 0
    @SerializedName("actor_name")
    var actorName = name
    @SerializedName("gender")
    var actorSex = sex
    @SerializedName("age")
    var actorAge = age
    @SerializedName("rating")
    var actorRating = rating
    @SerializedName("movies_appeared_in")
    var appearedIn = 0
}