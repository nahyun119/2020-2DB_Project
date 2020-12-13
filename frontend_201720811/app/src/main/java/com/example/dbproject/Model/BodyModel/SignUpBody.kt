package com.example.dbproject.Model.BodyModel

class SignUpBody(firstName:String,lastName:String,accountEmail:String,accountNum:String,accountId:String,accountPwd:String) {
    //: last_name, first_name, email, account_number(그냥 숫자 아무거나 넣으면 될 듯), password, account_id(사용자 로그인 시 필요한 아이디)
    var first_name  = firstName
    var last_name  = lastName
    var account_number =accountNum
    var password =accountPwd
    var account_id = accountId
    var email = accountEmail
}