package com.example.dbproject.Dialog

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.dbproject.Model.BodyModel.OrderMovieBody
import com.example.dbproject.Model.Movie
import com.example.dbproject.Model.OrderModel
import com.example.dbproject.R
import com.example.dbproject.Restful.SearchRetrofit
import com.example.dbproject.Restful.UserService
import com.example.dbproject.SharedData.UserData
import kotlinx.android.synthetic.main.dialog_order.*
import retrofit2.Call
import retrofit2.Response


class OrderDialog(val context: Context) {
    private val mDialog = Dialog(context)

    fun show(
        movieName: String,
        orderId: Int,
        list: ArrayList<Movie>
    ) {

        mDialog.setContentView(R.layout.dialog_order)
        mDialog.window!!.setBackgroundDrawableResource(R.color.colorTransparent)//모서리 둥글게할때 남은 부분 투명하게

        mDialog.show()

        //반납하기
        mDialog.btn_order_refund.setOnClickListener {

            val call: UserService = SearchRetrofit.getUserService()
            call.putUserOrderUpdate(OrderMovieBody( UserData.userInfo.userId,orderId.toString(),mDialog.rating_order.rating)).enqueue(object : retrofit2.Callback<OrderModel.OrderInfo> {
                override fun onFailure(call: Call<OrderModel.OrderInfo>, t: Throwable) {
                    Log.d("error", "$t")
                }

                override fun onResponse(call: Call<OrderModel.OrderInfo>, response: Response<OrderModel.OrderInfo>) {
                    if (response.isSuccessful) {
                        val order = response.body()!!
                        if(response.code()==200){
                            Toast.makeText(context,"$movieName 반납 성공", Toast.LENGTH_SHORT).show()
                            var returnIndex = -1
                            for(i in 0 until list.size){
                                if(list[i].orderId == order.orderItem.orderId){
                                    returnIndex = i
                                    break
                                }
                            }
                            if(returnIndex!=-1){
                                list.removeAt(returnIndex)
                            }
                            ratingListener.complete(list)
                            dismiss()
                        }
                    } else {
                        Toast.makeText(context,"$movieName 반납 실패. 재요청 해주세요.", Toast.LENGTH_SHORT).show()
                        println("response error : $response")
                    }
                }
            })


        }
    }


    fun dismiss() {
        if (mDialog.isShowing) {
            mDialog.dismiss()
        }
    }

    private lateinit var ratingListener: RatingListener
    fun setRatingListener(ratingSetListener:RatingListener ) {
        this.ratingListener = ratingSetListener
    }

    interface RatingListener {
        fun complete(list: ArrayList<Movie>)
    }

}