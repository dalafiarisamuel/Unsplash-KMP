package networking


import io.ktor.client.HttpClient
import io.ktor.util.AttributeKey
import io.ktor.client.request.*
import io.ktor.util.*
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.http.HttpHeaders

class AuthorizationTokenInterceptor(
    private val tokenProvider: suspend () -> String
) {

    @KtorDsl
    class Config {
        var tokenProvider: suspend () -> String = { "" }
    }

    @KtorDsl
    companion object Plugin: HttpClientPlugin<Config, AuthorizationTokenInterceptor> {
        override val key: AttributeKey<AuthorizationTokenInterceptor> =
            AttributeKey("AuthorizationTokenInterceptor")

        override fun prepare(block: Config.() -> Unit): AuthorizationTokenInterceptor {
            val config = Config().apply(block)
            return AuthorizationTokenInterceptor(config.tokenProvider)
        }

        override fun install(plugin: AuthorizationTokenInterceptor, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.State) {
                val token = plugin.tokenProvider()
                if (token.isNotEmpty()) {
                    context.headers.append(HttpHeaders.Authorization, "Client-ID $token")
                }
                proceed()
            }
        }
    }

}