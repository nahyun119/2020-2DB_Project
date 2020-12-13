package com.example.dbproject.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserModel {

    class UserInfo{
        @SerializedName("item")
        var user = User()
    }

    class User{
        @SerializedName("customer_id")
        var userId = ""
        @SerializedName("last_name")
        var lastName = ""
        @SerializedName("first_name")
        var firstName = ""
        @SerializedName("email")
        var userEmail  = ""

        @SerializedName("account_number")
        var userAccountNumber = ""
        @SerializedName("account_type")
        var userAccountType = ""
        @SerializedName("account_created_time")
        var userAccountCreateTime = ""
        @SerializedName("pwd")
        var userPwd = ""
        @SerializedName("account_id")
        var userAccountId= ""
        @Expose
        @SerializedName("is_expired")
        var checkExpired = 0
    }

}