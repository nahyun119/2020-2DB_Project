package com.example.dbproject.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dbproject.Adapter.MovieAdapter
import com.example.dbproject.Model.Movie
import com.example.dbproject.Model.MovieList
import com.example.dbproject.R
import com.example.dbproject.Restful.MovieService
import com.example.dbproject.Restful.SearchRetrofit
import kotlinx.android.synthetic.main.fragment_home.view.*
import retrofit2.Call
import retrofit2.Response

class HomeFragment : Fragment() {

    lateinit var ratingAdapter : MovieAdapter
    lateinit var typeAdapter : MovieAdapter

    var ratingList = ArrayList<Movie>()
    var typeList = ArrayList<Movie>()
    lateinit var mMainView : View

    val mSpinnerList = ArrayList<String>()
    var mSelectedType :String = "comedy"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        mMainView = root

        initAdapter()
        getMovieRatingList()
        getMovieTypeList()
        initSpinner()
        initButtonListener()
        return root
    }

    private fun initButtonListener() {
        mMainView.btn_search.setOnClickListener {
            getMovieTypeList()
            typeAdapter.notifyDataSetChanged()
        }
    }

    private fun initSpinner() {
        mSpinnerList.add("comedy")
        mSpinnerList.add("drama")
        mSpinnerList.add("action")
        mSpinnerList.add("foreign")
        val spinnerAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item,mSpinnerList)
        mMainView.spinner_home.adapter = spinnerAdapter
        mMainView.spinner_home.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when(p2){
                    0->{
                        //comedy
                        mSelectedType = "comedy"
                    }
                    1->{
                        //drama
                        mSelectedType = "drama"
                    }
                    2->{
                        //drama
                        mSelectedType = "action"
                    }
                    3->{
                        //drama
                        mSelectedType = "foreign"
                    }
                }
            }

        }
    }

    private fun getMovieTypeList() {
        val call: MovieService = SearchRetrofit.getMovieService()
        call.getMovieGenre(mSelectedType).enqueue(object :retrofit2.Callback<MovieList>{
            override fun onFailure(call: Call<MovieList>, t: Throwable) {
                Log.d("error","$t")
            }
            override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                if(response.isSuccessful){
                    val list = response.body()!!
                    typeList = list.movieList
                    println("장르별 영화 로드 성공")
                    typeAdapter.updateList(typeList)
                }
                else{
                    println("response error : $response")
                }
            }
        })
    }

    private fun getMovieRatingList() {
        val call: MovieService = SearchRetrofit.getMovieService()
        call.getMovieRating().enqueue(object :retrofit2.Callback<MovieList>{
            override fun onFailure(call: Call<MovieList>, t: Throwable) {
                Log.d("error","$t")
            }
            override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                if(response.isSuccessful){
                    val list = response.body()!!
                    ratingList = list.movieList
                    println("평점별 영화 로드 성공")
                    ratingAdapter.updateList(ratingList)
                }
                else{
                    println("response error : $response")
                }
            }
        })
    }

    private fun initAdapter() {
        ratingAdapter = MovieAdapter(this,ratingList)
        mMainView.recycler_rating.apply {
            adapter = ratingAdapter
            layoutManager =  LinearLayoutManager(context)
            hasFixedSize()
        }

        typeAdapter =  MovieAdapter(this,typeList)
        mMainView.recycler_type.apply {
            adapter = typeAdapter
            layoutManager =  LinearLayoutManager(context)
            hasFixedSize()
        }
    }

    private fun testMovie() {
        ratingList.add(Movie("The Godfather","Drama",3,5))
        ratingList.add(Movie("Shawshank Redemption","Drama",2,4))
        ratingList.add(Movie("Borat","Comedy",1,3))
    }

}