package com.ifyou.umorili.net

import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

interface API {
    @GET("get")
    fun getPosts(@Query("site") site: String = "bash.im", @Query("name") name: String = "bash"
                 , @Query("num") num: Int = 20): Observable<List<PostModel>>
}
