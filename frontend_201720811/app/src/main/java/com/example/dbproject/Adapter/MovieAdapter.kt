package com.example.dbproject.Adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.dbproject.Dialog.OrderDialog
import com.example.dbproject.Model.BodyModel.MovieBody
import com.example.dbproject.Model.Movie
import com.example.dbproject.Model.MovieList
import com.example.dbproject.R
import com.example.dbproject.Restful.SearchRetrofit
import com.example.dbproject.Restful.UserService
import com.example.dbproject.SharedData.UserData
import com.example.dbproject.ui.DetailActivity
import kotlinx.android.synthetic.main.item_movie.view.btn_unregister
import kotlinx.android.synthetic.main.item_movie.view.layout_title
import kotlinx.android.synthetic.main.item_movie2.view.*
import retrofit2.Call
import retrofit2.Response

class MovieAdapter(val fragment: Fragment, private var list:ArrayList<Movie>):RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    lateinit var fragmentName : String
    var orderDialog =  OrderDialog(fragment.requireContext())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieAdapter.ViewHolder {
        fragmentName = fragment.javaClass.simpleName

        when(fragmentName){
            "HomeFragment"->{
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
                return ViewHolder(view)
            }
            "Tab1Fragment"->{ //좋아하는 영화목록
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie2, parent, false)
                return ViewHolder(view)
            }
            else->{ // 대여한 영화 목록
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie2, parent, false)
                return ViewHolder(view)
            }
        }
    }

    fun updateList(movieList: ArrayList<Movie>){
        list.removeAll(list)
        list = movieList
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
        holder.itemView.layout_title.setOnClickListener {
            val intent = Intent(fragment.context, DetailActivity::class.java)
            val movie_id = list[position].movieId
            intent.putExtra("uid",movie_id)
            fragment.startActivity(intent)
        }

        holder.itemView.btn_unregister.setOnClickListener {
            //반납하기
            val movieName = list[position].movieName
            val order_id = list[position].orderId

            orderDialog.show(movieName,order_id,list)
            orderDialog.setRatingListener(object : OrderDialog.RatingListener{
                override fun complete(list: ArrayList<Movie>) {
                    updateList(list)
                    notifyDataSetChanged()
                }

            })
        }

        when(fragmentName){
            "Tab1Fragment"->{ //좋아하는 영화목록
                val movie_id = list[position].movieId

                holder.itemView.btn_delete.setOnClickListener {
                    val call: UserService = SearchRetrofit.getUserService()
                    call.deleteUserLike(MovieBody( UserData.userInfo.userId,movie_id.toString())).enqueue(object : retrofit2.Callback<MovieList> {
                        override fun onFailure(call: Call<MovieList>, t: Throwable) {
                            Log.d("error", "$t")
                        }

                        override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                            if (response.isSuccessful) {
                                val list = response.body()!!
                                Toast.makeText(fragment.context,"보고싶은 영화 삭제 성공", Toast.LENGTH_SHORT).show()
                                updateList(list.movieList)
                            } else {
                                println("response error : $response")
                            }
                        }
                    })
                }
            }
        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title = itemView.findViewById<TextView>(R.id.text_movie_title)
        var rating = itemView.findViewById<TextView>(R.id.text_movie_rating)
        var type = itemView.findViewById<TextView>(R.id.text_movie_type)
        var unregister = itemView.findViewById<LinearLayout>(R.id.btn_unregister)
        var delete = itemView.findViewById<LinearLayout>(R.id.btn_delete)
        var complete = itemView.findViewById<LinearLayout>(R.id.btn_register_complete)

        fun bind(position: Int) {
            title.text = list[position].movieName
            rating.text  = list[position].movieRating.toString()
            type.text = list[position].movieType

            when(fragmentName){
                "HomeFragment"->{
                    unregister.visibility = View.GONE
                }
                "Tab1Fragment"->{ //좋아하는 영화 목록
                    unregister.visibility = View.GONE
                    delete.visibility = View.VISIBLE
                    complete.visibility = View.GONE
                }
                "Tab2Fragment"->{ //대여한 영화 목록
                    unregister.visibility = View.VISIBLE
                    delete.visibility = View.GONE
                    if (list[position].checkReturn == 1) {
                        unregister.visibility = View.GONE
                        complete.visibility = View.VISIBLE

                    } else {
                        complete.visibility = View.GONE
                    }
                }
            }
        }
    }
}