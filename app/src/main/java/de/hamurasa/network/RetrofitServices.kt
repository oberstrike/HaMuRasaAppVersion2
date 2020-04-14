package de.hamurasa.network


import de.hamurasa.lesson.model.lesson.Lesson
import de.hamurasa.lesson.model.lesson.LessonDTO
import de.hamurasa.lesson.model.vocable.Vocable
import de.hamurasa.lesson.model.vocable.VocableDTO
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface VocableRetrofitService {

    @GET("/api/vocable/search")
    fun getWordsByText(@Query("value") value: String): Observable<List<Vocable>>

    @POST("/api/vocable")
    suspend fun addVocable(@Body vocableDTO: VocableDTO): VocableDTO

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
    @GET("/api/user/lessons")
    suspend fun getLessons(): List<Lesson>

    @POST("/api/lesson")
    suspend fun addNewLesson(@Body lesson: LessonDTO): LessonDTO?

    @POST("/api/lesson/{id}")
    suspend fun addVocableToLesson(@Path("id") id: Long, @Body vocableDTO: VocableDTO): ResponseBody

    @DELETE("/api/lesson/{id}")
    suspend fun deleteLesson(@Path("id") id: Long): ResponseBody

    @DELETE("/api/lesson/{id}/{vocableId}")
    suspend fun removeVocableFromLesson(@Path("id") id: Long, @Path("vocableId") vocableId: Long)
}

interface UpdateRetrofitService {
    @GET("/version")
    fun status(): Call<ResponseBody>
}


fun createVocableRetrofitService(username: String, password: String): VocableRetrofitService {
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

fun createUserRetrofitService(username: String, password: String): UserRetrofitService {
    return ServiceGenerator.createService(
        UserRetrofitService::class.java,
        username,
        password
    )
}

fun createUpdateRetrofitService(): UpdateRetrofitService {
    return ServiceGenerator.createService(UpdateRetrofitService::class.java)
}

open class User(val username: String, val password: String)

data class CustomResponse(val body: String)

object RetrofitServices {

    lateinit var userRetrofitService: UserRetrofitService

    lateinit var vocableRetrofitService: VocableRetrofitService

    lateinit var lessonRetrofitService: LessonRetrofitService

    lateinit var updateRetrofitService: UpdateRetrofitService

    fun init(username: String, password: String) {
        userRetrofitService = createUserRetrofitService(username, password)
    }

    fun initVocableRetrofitService(username: String, password: String) {
        vocableRetrofitService = createVocableRetrofitService(username, password)
    }

    fun initLessonRetrofitService(username: String, password: String) {
        lessonRetrofitService = createLessonRetrofitService(username, password)
    }

    fun initUpdateRetrofitService() {
        updateRetrofitService = createUpdateRetrofitService()
    }


}