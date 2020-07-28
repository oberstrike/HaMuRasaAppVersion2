package de.hamurasa


/*

internal class RetrofitServiceTest {

    private var mockWebServer = MockWebServer()

    private lateinit var userRetrofitService: UserRetrofitService


    @Before
    fun setUp() {
        mockWebServer.dispatcher = dispatcher
        mockWebServer.start(8080)
        val ip = mockWebServer.url("/")
        userRetrofitService = ServiceGenerator.createService(UserRetrofitService::class.java, "oberstrike", "mewtu1234")
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun loginTest() {
        val call =
            userRetrofitService.login().execute()
        Assert.assertTrue(call.isSuccessful)
    }

    @Test
    fun registerTest() {
        val extUser = object : User("oberstrike", "mewtu123") {
            val retypePassword = "mewtu123"
        }

        val call = userRetrofitService.register(extUser).blockingFirst()
        Assert.assertNotNull(call)
        Assert.assertEquals("Success", call.string())
    }

    private val dispatcher = object : QueueDispatcher() {
        @Throws(InterruptedException::class)
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when (request.path) {
                "/login" -> {
                    MockResponse()
                        .setResponseCode(HttpURLConnection.HTTP_OK)
                        .setBody("")
                }
                "/register" -> {
                    MockResponse()
                        .setResponseCode(HttpURLConnection.HTTP_OK)
                        .setBody("Success")
                }
                else -> MockResponse().setResponseCode(404)
            }
        }
    }
} */

