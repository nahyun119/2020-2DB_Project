package com.example.dbproject.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dbproject.Adapter.ActorAdapter
import com.example.dbproject.Model.*
import com.example.dbproject.Model.BodyModel.LikeMovieBody
import com.example.dbproject.Model.BodyModel.MovieBody
import com.example.dbproject.R
import com.example.dbproject.Restful.MovieService
import com.example.dbproject.Restful.SearchRetrofit
import com.example.dbproject.Restful.UserService
import com.example.dbproject.SharedData.UserData
import kotlinx.android.synthetic.main.activity_detail.*
import retrofit2.Call
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    lateinit var actorAdapter: ActorAdapter


    var movie_id = -1

    var movie = Movie()
    var actorList = ArrayList<ActorModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        movie_id = intent.getIntExtra("uid",0)

        getMovieInfo()
        initClickListener()
        initAdapter()
    }

    private fun getMovieInfo() {
        val call: MovieService = SearchRetrofit.getMovieService()
        call.getMovieInfo(movie_id).enqueue(object :retrofit2.Callback<MovieInfo>{
            override fun onFailure(call: Call<MovieInfo>, t: Throwable) {
                Log.d("error","$t")
            }
            override fun onResponse(call: Call<MovieInfo>, response: Response<MovieInfo>) {
                if(response.isSuccessful){
                    val list = response.body()!!
                    movie.movieName = list.movieItem.movieName
                    movie.movieId = list.movieItem.movieId
                    movie.copies = list.movieItem.copies
                    movie.movieRating = list.movieItem.movieRating
                    movie.movieType = list.movieItem.movieType

                    actorList = list.movieItem.actorList
                    initMovieView()
                    actorAdapter.updateList(actorList)
                }
                else{
                    println("response error : $response")
                }
            }
        })
    }

    private fun initMovieView() {
        text_detail_name.text = movie.movieName
        text_detail_type.text = movie.movieType
        rating_detail.rating = movie.movieRating.toFloat()
    }

    private fun initClickListener() {
        btn_register_detail.setOnClickListener {
            //대여하기
            val call: UserService = SearchRetrofit.getUserService()
            call.postUserOrder(MovieBody(UserData.userInfo.userId,movie_id.toString())).enqueue(object :retrofit2.Callback<OrderModel.OrderInfo>{
                override fun onFailure(call: Call<OrderModel.OrderInfo>, t: Throwable) {
                    println("response error11 : $t")
                }
                override fun onResponse(call: Call<OrderModel.OrderInfo>, response: Response<OrderModel.OrderInfo>) {
                    println("response: $response")
                    if(response.isSuccessful){
                        val list = response.body()!!
                        Toast.makeText(this@DetailActivity,"${movie.movieName }대여 성공 ",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        println("response error : $response")
                        Toast.makeText(this@DetailActivity,"${movie.movieName }대여 실패(요금제가 없거나 혹은 유효하지 않거나 사용가능한 대여 횟수가 없습니다.)",Toast.LENGTH_LONG).show()
                    }
                }
            })
        }
        btn_register_like.setOnClickListener {
            //좋아요 버튼
            val weight = input_weight.text.toString()
            if(weight.isEmpty()){
                Toast.makeText(this,"가중치를 입력하세요(최대 10)",Toast.LENGTH_SHORT).show()
            }
            else {
                val call: UserService = SearchRetrofit.getUserService()
                call.postUserLike(LikeMovieBody(UserData.userInfo.userId,movie_id.toString(),weight.toInt())).enqueue(object : retrofit2.Callback<MovieList> {
                    override fun onFailure(call: Call<MovieList>, t: Throwable) {
                        Log.d("error", "$t")
                    }

                    override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                        if (response.isSuccessful) {
                            val list = response.body()!!
                            Toast.makeText(this@DetailActivity,"보고싶은 영화 추가 성공",Toast.LENGTH_SHORT).show()
                            getMovieInfo()
                        } else {
                            println("response error : $response")
                        }
                    }
                })
            }
        }
    }

    private fun initAdapter() {
        actorAdapter = ActorAdapter(this,actorList)
        recycler_actor.apply {
            adapter = actorAdapter
            layoutManager = LinearLayoutManager(this@DetailActivity)
            hasFixedSize()
        }
    }

}