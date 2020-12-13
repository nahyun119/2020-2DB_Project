package com.example.dbproject.Restful

import android.text.TextUtils
import com.example.dbproject.Model.Movie
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SearchRetrofit { // 오브젝트는 싱글톤 패턴

    //RetrofitService를 연결

    fun getMovieService() : MovieService = retrofit.create(
        MovieService::class.java
    )

    fun getUserService(): UserService = retrofit.create(
        UserService::class.java
    )

    private val gson = GsonBuilder().registerTypeAdapterFactory(NullStringToEmptyAdapterFactory()).serializeNulls().create()

    private val retrofit = Retrofit.Builder().baseUrl("http://192.168.35.173:3000") // base url
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

}

    /** 받아오는 json 값중에 null이 있는지 확인 : 있으면 default값인 ""값으로 바꿈 **/
    class NullStringToEmptyAdapterFactory : TypeAdapterFactory {
        override fun <T : Any?> create(gson: Gson?, type: TypeToken<T>?): TypeAdapter<T>? {
            val rawType: Class<T> = type?.rawType as Class<T>
            if (rawType != String::class.java) {
                return null
            }
            return StringAdapter() as TypeAdapter<T>
        }
    }

    class StringAdapter : TypeAdapter<String>() {
        override fun write(out: JsonWriter?, value: String?) {
            if (TextUtils.isEmpty(value)) {
                out?.nullValue()
                return
            }
            out!!.value(value)
        }

    override fun read(`in`: JsonReader?): String {
        if (`in`?.peek() == JsonToken.NULL) {
            `in`.nextNull()
            return ""
        }
        return `in`!!.nextString()
    }

}