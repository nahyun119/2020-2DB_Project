package com.example.dbproject.ui.mypage.Tab

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dbproject.Adapter.MovieAdapter
import com.example.dbproject.Model.Movie
import com.example.dbproject.Model.MovieList
import com.example.dbproject.R
import com.example.dbproject.Restful.SearchRetrofit
import com.example.dbproject.Restful.UserService
import com.example.dbproject.SharedData.UserData
import kotlinx.android.synthetic.main.fragment_tab1.view.*
import retrofit2.Call
import retrofit2.Response

class Tab1Fragment : Fragment() {

    lateinit var likeAdapter : MovieAdapter

    var likeMovieList = ArrayList<Movie>()
    lateinit var mMainView : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tab1, container, false)
        mMainView = view

        initAdapter()
        getUserLikeList()

        return view
    }

    private fun getUserLikeList() {
        val call: UserService = SearchRetrofit.getUserService()
        call.getUserLikeList(UserData.userInfo.userId).enqueue(object :retrofit2.Callback<MovieList>{
            override fun onFailure(call: Call<MovieList>, t: Throwable) {
                Log.d("error","$t")
            }
            override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                if(response.isSuccessful){
                    val list = response.body()!!
                    likeMovieList = list.movieList
                    println("좋아하는 영화 로드 성공")
                    likeAdapter.updateList(likeMovieList)
                }
                else{
                    println("response error : $response")
                }
            }
        })
    }

    private fun initAdapter() {
        likeAdapter = MovieAdapter(this,likeMovieList)
        mMainView.recycler_like.apply {
            adapter = likeAdapter
            layoutManager =  LinearLayoutManager(context)
            hasFixedSize()
        }
    }



}