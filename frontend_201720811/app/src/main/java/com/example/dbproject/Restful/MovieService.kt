package com.example.dbproject.Restful

import com.example.dbproject.Model.MovieInfo
import com.example.dbproject.Model.MovieList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface MovieService {

    //영화 평점 순 정렬
    @Headers("accept: application/json")
    @GET("/api/movie/list/rating") fun getMovieRating(): Call<MovieList>

    //영화 장르별 정렬
    @Headers("accept: application/json")
    @GET("/api/movie/list/genre") fun getMovieGenre(@Query("genre") type : String): Call<MovieList>

    //영화 상세 정보
    @Headers("accept: application/json")
    @GET("/api/movie/info") fun getMovieInfo(@Query("movie_id") id : Int): Call<MovieInfo>

}