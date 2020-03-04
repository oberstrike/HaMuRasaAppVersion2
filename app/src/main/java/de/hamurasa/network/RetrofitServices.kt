package de.hamurasa.network


import de.hamurasa.lesson.model.Vocable
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface VocableRetrofitService {
    @GET("/api/words")
    fun getWordsByText(@Query("text") text: String): Observable<List<Vocable>>

    @GET("/api/words/translation")
    fun getWordsByTranslation(@Query("text") ext: String): Observable<List<Vocable>>
}

interface UserRetrofitService {

    @POST("/register")
    fun register(@Body user: User): Observable<ResponseBody>

    @GET("/login")
    fun login(): Call<ResponseBody>

}

interface LessonRetrofitService {
    @GET("/api/student/lessons")
    fun getLessons(): Call<ResponseBody>
}


fun createVocableService(username: String, password: String): VocableRetrofitService {
    return ServiceGenerator.createService(
        VocableRetrofitService::class.java,
        username,
        password
    )
}

fun createLessonRetrofitService(username: String, password: String): LessonRetrofitService {
    return ServiceGenerator.createService(
        LessonRetrofitService::class.java,
        username,
        password
    )
}

fun createUserService(username: String, password: String): UserRetrofitService {
    return ServiceGenerator.createService(UserRetrofitService::class.java,
        username,
        password)
}

open class User(val username: String, val password: String)

data class CustomResponse(val body: String)

object RetrofitServices {

    lateinit var userRetrofitService: UserRetrofitService

    lateinit var vocableRetrofitService: VocableRetrofitService

    lateinit var lessonRetrofitService: LessonRetrofitService

    fun init(username: String, password: String) {
        userRetrofitService = createUserService(username, password)
    }

    fun initVocableRetrofitService(username: String, password: String) {
        vocableRetrofitService = createVocableService(username, password)
    }

    fun initLessonRetrofitService(username: String, password: String){
        lessonRetrofitService = createLessonRetrofitService(username, password)
    }



}