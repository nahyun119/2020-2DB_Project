package com.example.dbproject.Dialog

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.dbproject.Model.BodyModel.AccountBody
import com.example.dbproject.Model.BodyModel.SignUpBody
import com.example.dbproject.Model.UserModel
import com.example.dbproject.R
import com.example.dbproject.Restful.SearchRetrofit
import com.example.dbproject.Restful.UserService
import com.example.dbproject.SharedData.UserData
import kotlinx.android.synthetic.main.dialog_account.*
import kotlinx.android.synthetic.main.dialog_sign_up.*
import retrofit2.Call
import retrofit2.Response


class SignUpDialog(val context: Context) {

    private val mDialog = Dialog(context)

    fun show() {

        mDialog.setContentView(R.layout.dialog_sign_up)
        mDialog.window!!.setBackgroundDrawableResource(R.color.colorTransparent)//모서리 둥글게할때 남은 부분 투명하게

        mDialog.show()

        //가입하기
        mDialog.btn_sign_up.setOnClickListener {

            val lastName = mDialog.input_last_name.text.toString()
            val firstName = mDialog.input_first_name.text.toString()
            val email = mDialog.input_email.text.toString()
            val accountNumber = 0
            val id = mDialog.input_id.text.toString()
            val pwd = mDialog.input_password.text.toString()

            if(lastName.isNullOrEmpty() || firstName.isNullOrEmpty() || email.isNullOrEmpty() || id.isNullOrEmpty() || pwd.isNullOrEmpty()){
                Toast.makeText(context,"빈칸없이 입력해주세요.",Toast.LENGTH_SHORT).show()
            }
            else{
            val call: UserService = SearchRetrofit.getUserService()
            call.postSignUp(SignUpBody(firstName,lastName,email,accountNumber.toString(),id,pwd)).enqueue(object :retrofit2.Callback<UserModel.User>{
                override fun onFailure(call: Call<UserModel.User>, t: Throwable) {
                    Log.d("error","$t")
                }
                override fun onResponse(call: Call<UserModel.User>, response: Response<UserModel.User>) {
                    if(response.isSuccessful){
                        val body = response.body()!!
                        if(response.code()==200){
                            Toast.makeText(context,"회원 가입 성공.",Toast.LENGTH_SHORT).show()
                            dismiss()
                        }

                    }
                    else{
                        Toast.makeText(context,"회원 가입 실패. 재요청 해주세요.",Toast.LENGTH_SHORT).show()
                        println("response error : $response")
                    }
                }
            })
            }

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