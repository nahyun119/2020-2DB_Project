package com.example.dbproject.Restful

import com.example.dbproject.Model.BodyModel.*
import com.example.dbproject.Model.MovieList
import com.example.dbproject.Model.MovieInfo
import com.example.dbproject.Model.OrderModel
import com.example.dbproject.Model.UserModel
import retrofit2.Call
import retrofit2.http.*

interface UserService {

    //회원가입
    @POST("/api/user/signup") fun postSignUp(@Body signUpBody : SignUpBody) : Call<UserModel.User>

    //로그인
    @POST("/api/user/login") fun postLogin(@Body loginBody : LoginBody) : Call<UserModel.UserInfo>
    // 사용자 요금제 가입
    @POST("/api/user/account") fun postAccount(@Body accountBody: AccountBody) : Call<UserModel.UserInfo>
    //사용자 정보
    @GET("/api/user/info") fun getUserInfo(@Query("customer_id") id : String) : Call<UserModel.UserInfo>
    //사용자 대여한 영화 목록
    @GET("/api/user/list/order") fun getUserOrderList(@Query("customer_id") id : String) : Call<MovieList>
    //사용자 좋아하는 영화 목록
    @GET("/api/user/list/movie") fun getUserLikeList(@Query("customer_id") id : String) : Call<MovieList>
    // 사용자 좋아하는 영화 추가
    @POST("/api/user/like/movie") fun postUserLike(@Body likeMovieBody : LikeMovieBody) : Call<MovieList>
    //사용자 좋아하는 영화 삭제
    @HTTP(method = "DELETE", path = "/api/user/like/movie", hasBody = true)
    fun deleteUserLike(@Body movieBody: MovieBody) : Call<MovieList>
    //사용자 영화 대여
    @POST("/api/user/order") fun postUserOrder(@Body movieBody: MovieBody) : Call<OrderModel.OrderInfo>

    //사용자 대여한 영화 반납
    @PUT("/api/user/order") fun putUserOrderUpdate(@Body orderMovieBody: OrderMovieBody) : Call<OrderModel.OrderInfo>

}