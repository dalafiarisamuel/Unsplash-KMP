package data.repository

import data.remote.repository.ImageRepository
import data.remote.repository.Resource
import di.mapperModule
import di.networkModule
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import networking.createApiInterface
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import util.NetworkHelper
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ImageRepositoryImplTest : KoinTest {

    private lateinit var sut: ImageRepository

    @BeforeTest
    fun setUp() {
        startKoin { modules(networkModule(), mapperModule()) }
    }

    @Test
    fun givenSuccessResponse_whenGetImageSearchResultIsCalled_successStateIsReturned() = runTest {
        val ktorfit =
            NetworkHelper.createKtorfitWithMockResponse(
                HttpStatusCode.OK,
                NetworkHelper.successfulImageListResponse,
            )

        val apiService = ktorfit.createApiInterface()

        sut = ImageRepositoryImpl(apiService)
        val response = sut.getImageSearchResult("Nigeria", 1, 2)
        assertNotNull(response)
        assertTrue(response is Resource.Success)
    }

    @Test
    fun givenSuccessResponse_whenGetPhotoIsCalled_successStateIsReturned() = runTest {
        val ktorfit =
            NetworkHelper.createKtorfitWithMockResponse(
                HttpStatusCode.OK,
                NetworkHelper.successfulImageResponse,
            )

        val apiService = ktorfit.createApiInterface()

        sut = ImageRepositoryImpl(apiService)
        val response = sut.getPhoto("Nigeria")
        assertNotNull(response)
        assertTrue(response is Resource.Success)
    }

    @Test
    fun givenErrorResponse_whenGetPhotoIsCalled_failureStateIsReturned() = runTest {
        val ktorfit =
            NetworkHelper.createKtorfitWithMockResponse(
                HttpStatusCode.Unauthorized,
                NetworkHelper.authorizationError,
            )

        val apiService = ktorfit.createApiInterface()

        sut = ImageRepositoryImpl(apiService)
        val response = sut.getPhoto("vzpEjttBI0M")
        assertNotNull(response)
        assertTrue(response is Resource.Failure)
    }

    @Test
    fun givenErrorResponse_whenGetImageSearchResultIsCalled_failureStateIsReturned() = runTest {
        val ktorfit =
            NetworkHelper.createKtorfitWithMockResponse(
                HttpStatusCode.OK,
                NetworkHelper.authorizationError,
            )

        val apiService = ktorfit.createApiInterface()

        sut = ImageRepositoryImpl(apiService)
        val response = sut.getImageSearchResult("Nigeria", 1, 2)
        assertNotNull(response)
        assertTrue(response is Resource.Failure)
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }
}
