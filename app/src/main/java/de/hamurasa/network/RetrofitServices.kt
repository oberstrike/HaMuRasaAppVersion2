package de.hamurasa.network


import de.hamurasa.lesson.model.lesson.Lesson
import de.hamurasa.lesson.model.lesson.LessonDTO
import de.hamurasa.lesson.model.vocable.Vocable
import de.hamurasa.lesson.model.vocable.VocableDTO
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface VocableRetrofitService {

    @GET("/api/vocable/search")
    suspend fun getWordsByText(@Query("value") value: String): List<Vocable>

    @POST("/api/vocable")
    suspend fun addVocable(@Body vocableDTO: VocableDTO): VocableDTO

    @GET("/api/words/translation")
    suspend fun getWordsByTranslation(@Query("text") ext: String): List<Vocable>

    @PATCH("/api/vocable/{id}")
    suspend fun patchWord(@Query("id") id: Long, @Body vocableDTO: VocableDTO)

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

    @DELETE("/api/lesson/{id}/vocable/{vocableId}")
    suspend fun removeVocableFromLesson(
        @Path("id") id: Long,
        @Path("vocableId") vocableId: Long
    )

    @PATCH("/api/lesson/{id}")
    suspend fun patchLesson(@Path("id") id: Long, @Body lessonDTO: LessonDTO): Lesson
}

interface UpdateRetrofitService {
    @GET("/version")
    suspend fun status(): ResponseBody
}

inline fun <reified T> createRetrofitService(
    username: String? = null,
    password: String? = null
): T {
    return ServiceGenerator.createService(T::class.java, username, password)
}


open class User(val username: String, val password: String)


object RetrofitServices {

    lateinit var userRetrofitService: UserRetrofitService

    lateinit var vocableRetrofitService: VocableRetrofitService

    lateinit var lessonRetrofitService: LessonRetrofitService

    lateinit var updateRetrofitService: UpdateRetrofitService

    fun init(username: String, password: String) {
        userRetrofitService = createRetrofitService(username, password)
    }

    fun initVocableRetrofitService(username: String, password: String) {
        vocableRetrofitService = createRetrofitService(username, password)
    }

    fun initLessonRetrofitService(username: String, password: String) {
        lessonRetrofitService = createRetrofitService(username, password)
    }

    fun initUpdateRetrofitService() {
        updateRetrofitService = createRetrofitService()
    }

}


