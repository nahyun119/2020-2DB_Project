package com.example.dbproject.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import com.example.dbproject.Dialog.SignUpDialog
import com.example.dbproject.MainActivity
import com.example.dbproject.Model.BodyModel.LoginBody
import com.example.dbproject.Model.UserModel
import com.example.dbproject.R
import com.example.dbproject.Restful.SearchRetrofit
import com.example.dbproject.Restful.UserService
import com.example.dbproject.SharedData.UserData
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    var userId = ""
    var userPwd = ""

    lateinit var signUpDialog: SignUpDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()
        signUpDialog = SignUpDialog(this)
    }

    private fun initView() {
        input_id.addTextChangedListener(TextWatcher(R.id.input_id))
        input_pwd.addTextChangedListener(TextWatcher(R.id.input_pwd))

        btn_login.setOnClickListener {

            val call: UserService = SearchRetrofit.getUserService()
            call.postLogin(LoginBody(userId,userPwd)).enqueue(object :retrofit2.Callback<UserModel.UserInfo>{
                override fun onFailure(call: Call<UserModel.UserInfo>, t: Throwable) {
                    Log.d("error","$t")
                }
                override fun onResponse(call: Call<UserModel.UserInfo>, response: Response<UserModel.UserInfo>) {
                    if(response.isSuccessful){
                        //로그인 성공처리
                        val response = response.body()!!
                        UserData.userInfo = response.user
                        println("로그인 : ${response.user.userId}")
                        val intent = Intent(this@LoginActivity,MainActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this@LoginActivity,"비밀번호 오류. 다시 입력해주세요.",Toast.LENGTH_SHORT).show()
                        println("response error : $response")
                    }
                }
            })

        }

        btn_sign_up.setOnClickListener {
            signUpDialog.show()
        }
    }

    inner class TextWatcher(val id:Int = -1) : android.text.TextWatcher {

        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
           when(id){
               R.id.input_id->{
                   userId = s.toString()
               }
               R.id.input_pwd->{
                   userPwd = s.toString()
               }
           }
        }
    }
}