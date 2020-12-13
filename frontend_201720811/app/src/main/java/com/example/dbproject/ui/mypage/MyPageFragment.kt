package com.example.dbproject.ui.mypage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.dbproject.Adapter.ViewPagerAdapter
import com.example.dbproject.Dialog.AccountDialog
import com.example.dbproject.Model.UserModel
import com.example.dbproject.R
import com.example.dbproject.Restful.SearchRetrofit
import com.example.dbproject.Restful.UserService
import com.example.dbproject.SharedData.UserData
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_mypage.view.*
import retrofit2.Call
import retrofit2.Response


class MyPageFragment : Fragment() {

    lateinit var mMainView : View
    lateinit var accountDialog : AccountDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accountDialog = AccountDialog(requireContext())
        initListener()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_mypage, container, false)
        mMainView = root

        getUserInfo()
        initTabView()
        return root
    }

    private fun initListener() {
        accountDialog.setJoinListener(object : AccountDialog.JoinListener{
            override fun complete() {
               checkAccountType()
            }
        })
    }

    private fun initClickListener() {
        mMainView.btn_register.setOnClickListener {
            accountDialog.show()
        }
    }


    private fun getUserInfo() {
        val call: UserService = SearchRetrofit.getUserService()
        call.getUserInfo(UserData.userInfo.userId).enqueue(object :retrofit2.Callback<UserModel.UserInfo>{
            override fun onFailure(call: Call<UserModel.UserInfo>, t: Throwable) {
                Log.d("error","$t")
            }
            override fun onResponse(call: Call<UserModel.UserInfo>, response: Response<UserModel.UserInfo>) {
                if(response.isSuccessful){
                    val response  = response.body()!!
                    UserData.userInfo = response.user
                    println("user 정보 로드 성공")
                    initUserView(UserData.userInfo)
                    checkAccountType()
                }
                else{
                    println("response error : $response")
                }
            }
        })
    }

    private fun checkAccountType() {
        if(!UserData.userInfo.userAccountType.isNullOrEmpty()){
            mMainView.btn_register.visibility = View.GONE
        }
        else{
            mMainView.text_user_account_type.text = "없음"
            initClickListener()
        }
    }

    private fun initTabView() {
        val fragmentAdapter = ViewPagerAdapter(requireActivity().supportFragmentManager,2)
        mMainView.viewpager.adapter = fragmentAdapter

        val tabs = mMainView.findViewById<TabLayout>(R.id.tab_layout)
        tabs.setupWithViewPager(mMainView.viewpager)
    }

    private fun initUserView(userModel :UserModel.User) {
        Glide.with(this).load(R.drawable.user).circleCrop().into(mMainView.img_user)
        mMainView.text_user_name.text = userModel.firstName
        mMainView.text_user_id.text = userModel.userAccountId
        mMainView.text_user_account_type.text = userModel.userAccountType
        mMainView.text_user_email.text = userModel.userEmail
    }
}