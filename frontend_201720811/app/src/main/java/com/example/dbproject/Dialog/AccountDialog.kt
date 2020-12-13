package com.example.dbproject.Dialog

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.dbproject.Model.BodyModel.AccountBody
import com.example.dbproject.Model.UserModel
import com.example.dbproject.R
import com.example.dbproject.Restful.SearchRetrofit
import com.example.dbproject.Restful.UserService
import com.example.dbproject.SharedData.UserData
import kotlinx.android.synthetic.main.dialog_account.*
import retrofit2.Call
import retrofit2.Response


class AccountDialog(val context: Context) {

    private val mDialog = Dialog(context)

    fun show() {

        mDialog.setContentView(R.layout.dialog_account)
        mDialog.window!!.setBackgroundDrawableResource(R.color.colorTransparent)//모서리 둥글게할때 남은 부분 투명하게

        mDialog.show()

        //가입하기
        mDialog.btn_join.setOnClickListener {
            var accountType = ""
            if (mDialog.checkbox_limited.isChecked && mDialog.checkbox_unlimited.isChecked) {
                Toast.makeText(context, "하나의 요금제만 선택해주세요", Toast.LENGTH_SHORT).show()
            } else if (!mDialog.checkbox_limited.isChecked && !mDialog.checkbox_unlimited.isChecked) {
                Toast.makeText(context, "요금제를 선택해주세요", Toast.LENGTH_SHORT).show()
            } else {
                if (mDialog.checkbox_unlimited.isChecked) {
                    accountType = "unlimited"

                } else if(mDialog.checkbox_limited.isChecked){
                    accountType = "limited"
                }
            }
            val call: UserService = SearchRetrofit.getUserService()
            call.postAccount(AccountBody(UserData.userInfo.userId,accountType)).enqueue(object :retrofit2.Callback<UserModel.UserInfo>{
                override fun onFailure(call: Call<UserModel.UserInfo>, t: Throwable) {
                    Log.d("error","$t")
                }
                override fun onResponse(call: Call<UserModel.UserInfo>, response: Response<UserModel.UserInfo>) {
                    if(response.isSuccessful){
                        val response = response.body()!!
                        Toast.makeText(context,"요금제 가입 성공",Toast.LENGTH_SHORT).show()
                        UserData.userInfo = response.user
                        joinListener.complete()
                        dismiss()
                    }
                    else{
                        Toast.makeText(context,"가입 요청 오류. 다시 요청해주세요.",Toast.LENGTH_SHORT).show()
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

    private lateinit var joinListener: JoinListener
    fun setJoinListener(joinSetListener:JoinListener ) {
        this.joinListener = joinSetListener
    }

    interface JoinListener {
        fun complete()
    }

}