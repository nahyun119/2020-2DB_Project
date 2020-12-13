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
import kotlinx.android.synthetic.main.fragment_tab2.view.*
import retrofit2.Call
import retrofit2.Response

class Tab2Fragment : Fragment() {

    lateinit var orderAdapter : MovieAdapter

    var orderMovieList = ArrayList<Movie>()
    lateinit var mMainView : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tab2, container, false)
        mMainView = view
        initAdapter()
        getUserOrderList()
        return view
    }

    private fun getUserOrderList() {
        val call: UserService = SearchRetrofit.getUserService()
        call.getUserOrderList(UserData.userInfo.userId).enqueue(object :retrofit2.Callback<MovieList>{
            override fun onFailure(call: Call<MovieList>, t: Throwable) {
                Log.d("error","$t")
            }
            override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                if(response.isSuccessful){
                    val list = response.body()!!
                    orderMovieList = list.movieList
                    println("대여한 영화 로드 성공")
                    orderAdapter.updateList(orderMovieList)
                }
                else{
                    println("response error : $response")
                }
            }
        })
    }

    private fun initAdapter() {
        orderAdapter = MovieAdapter(this,orderMovieList)
        mMainView.recycler_order.apply {
            adapter = orderAdapter
            layoutManager =  LinearLayoutManager(context)
            hasFixedSize()
        }
    }

}