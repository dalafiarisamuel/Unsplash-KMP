package util

import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import org.koin.test.KoinTest
import org.koin.test.get

object NetworkHelper : KoinTest {

    fun createKtorfitWithMockResponse(
        status: HttpStatusCode,
        content: String,
    ): Ktorfit {

        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel(content),
                status = status,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )

        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(get())
            }
        }

        return Ktorfit.Builder().build {
            httpClient(client)
            baseUrl("http://127.0.0.1/")
        }
    }

    val successfulImageListResponse: String
        get() = "{\"total\":2284,\"total_pages\":1142,\"results\":[{\"id\":\"vzpEjttBI0M\",\"slug\"" +
                ":\"text-vzpEjttBI0M\",\"alternative_slugs\":{\"en\":\"text-vzpEjttBI0M\",\"es\":" +
                "\"texto-vzpEjttBI0M\",\"ja\":\"文章-vzpEjttBI0M\",\"fr\":\"texte-vzpEjttBI0M\",\"it\"" +
                ":\"testo-vzpEjttBI0M\",\"ko\":\"문자-메시지-vzpEjttBI0M\",\"de\":\"text-vzpEjttBI0M\",\"pt\"" +
                ":\"texto-vzpEjttBI0M\"},\"created_at\":\"2020-10-10T11:10:29Z\",\"updated_at\"" +
                ":\"2024-07-26T09:54:31Z\",\"promoted_at\":null,\"width\":3329,\"height\":4661,\"color\"" +
                ":\"#262626\",\"blur_hash\":\"LDHBDE,^E1VttQ~Cr]s;I*Im-:Sw\",\"description\"" +
                ":\"On going Protest in Nigeria \uD83C\uDDF3\uD83C\uDDEC to End SARS killing,\",\"alt_description\"" +
                ":\"text\",\"breadcrumbs\":[],\"urls\":{\"raw\":\"https://images.unsplash.com/photo-1602327850706-eadbbd6a1522?ixid=M3wyNDI1MTl8MHwxfHNlYXJjaHwxfHxOaWdlcmlhfGVufDB8fHx8MTcyMjUxODIxOXww&ixlib=rb-4.0.3\",\"full\"" +
                ":\"https://images.unsplash.com/photo-1602327850706-eadbbd6a1522?crop=entropy&cs=srgb&fm=jpg&ixid=M3wyNDI1MTl8MHwxfHNlYXJjaHwxfHxOaWdlcmlhfGVufDB8fHx8MTcyMjUxODIxOXww&ixlib=rb-4.0.3&q=85\",\"regular\"" +
                ":\"https://images.unsplash.com/photo-1602327850706-eadbbd6a1522?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wyNDI1MTl8MHwxfHNlYXJjaHwxfHxOaWdlcmlhfGVufDB8fHx8MTcyMjUxODIxOXww&ixlib=rb-4.0.3&q=80&w=1080\",\"small\"" +
                ":\"https://images.unsplash.com/photo-1602327850706-eadbbd6a1522?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wyNDI1MTl8MHwxfHNlYXJjaHwxfHxOaWdlcmlhfGVufDB8fHx8MTcyMjUxODIxOXww&ixlib=rb-4.0.3&q=80&w=400\",\"thumb\"" +
                ":\"https://images.unsplash.com/photo-1602327850706-eadbbd6a1522?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wyNDI1MTl8MHwxfHNlYXJjaHwxfHxOaWdlcmlhfGVufDB8fHx8MTcyMjUxODIxOXww&ixlib=rb-4.0.3&q=80&w=200\",\"small_s3\"" +
                ":\"https://s3.us-west-2.amazonaws.com/images.unsplash.com/small/photo-1602327850706-eadbbd6a1522\"},\"links\":{\"self\":\"https://api.unsplash.com/photos/text-vzpEjttBI0M\",\"html\":\"https://unsplash.com/photos/text-vzpEjttBI0M\",\"download\"" +
                ":\"https://unsplash.com/photos/vzpEjttBI0M/download?ixid=M3wyNDI1MTl8MHwxfHNlYXJjaHwxfHxOaWdlcmlhfGVufDB8fHx8MTcyMjUxODIxOXww\",\"download_location\":\"https://api.unsplash.com/photos/vzpEjttBI0M/download?ixid=M3wyNDI1MTl8MHwxfHNlYXJjaHwxfHxOaWdlcmlhfGVufDB8fHx8MTcyMjUxODIxOXww\"},\"likes\"" +
                ":22,\"liked_by_user\":false,\"current_user_collections\":[],\"sponsorship\":null,\"topic_submissions\":{\"current-events\":{\"status\":\"rejected\"},\"street-photography\":{\"status\":\"rejected\"}},\"asset_type\":\"photo\",\"user\":{\"id\":\"d2X4tWD_Xjs\",\"updated_at\"" +
                ":\"2024-06-20T18:06:29Z\",\"username\":\"magicconceptstudio\",\"name\":\"Tobi Oshinnaike\",\"first_name\":\"Tobi\",\"last_name\":\"Oshinnaike\",\"twitter_username\":\"Magicconceptst1\",\"portfolio_url\":\"https://unsplash.com/magicconcept\",\"bio\"" +
                ":\"• PORTRAIT \\r\\n• PRODUCT  • DOCUMENTARY Open for Collaboration  I rep \uD83C\uDDF3\uD83C\uDDEC\",\"location\":\"Lagos Nigeria\",\"links\"" +
                ":{\"self\":\"https://api.unsplash.com/users/magicconceptstudio\",\"html\":\"https://unsplash.com/@magicconceptstudio\",\"photos\":\"https://api.unsplash.com/users/magicconceptstudio/photos\",\"likes\"" +
                ":\"https://api.unsplash.com/users/magicconceptstudio/likes\",\"portfolio\":\"https://api.unsplash.com/users/magicconceptstudio/portfolio\",\"following\"" +
                ":\"https://api.unsplash.com/users/magicconceptstudio/following\",\"followers\":\"https://api.unsplash.com/users/magicconceptstudio/followers\"},\"profile_image\":{\"small\"" +
                ":\"https://images.unsplash.com/profile-1620192120692-3b6df822e182image?ixlib=rb-4.0.3&crop=faces&fit=crop&w=32&h=32\",\"medium\"" +
                ":\"https://images.unsplash.com/profile-1620192120692-3b6df822e182image?ixlib=rb-4.0.3&crop=faces&fit=crop&w=64&h=64\",\"large\":\"https://images.unsplash.com/profile-1620192120692-3b6df822e182image?ixlib=rb-4.0.3&crop=faces&fit=crop&w=128&h=128\"}" +
                ",\"instagram_username\":\"Magicconceptstudio\",\"total_collections\":3,\"total_likes\":78,\"total_photos\":64,\"total_promoted_photos\":0,\"total_illustrations\"" +
                ":0,\"total_promoted_illustrations\":0,\"accepted_tos\":true,\"for_hire\":true,\"social\":{\"instagram_username\"" +
                ":\"Magicconceptstudio\",\"portfolio_url\":\"https://unsplash.com/magicconcept\",\"twitter_username\":\"Magicconceptst1\",\"paypal_email\":null}},\"tags\":[{\"type\":\"search\",\"title\"" +
                ":\"nigeria\"},{\"type\":\"search\",\"title\":\"lagos\"},{\"type\":\"search\",\"title\":\"police brutality\"}]},{\"id\":\"MtNEziYlm2U\",\"slug\"" +
                ":\"group-of-people-standing-on-gray-concrete-floor-during-daytime-MtNEziYlm2U\",\"alternative_slugs\":{\"en\":\"group-of-people-standing-on-gray-concrete-floor-during-daytime-MtNEziYlm2U\",\"es\":\"grupo-de-personas-de-pie-en-el-piso-de-concreto-gris-durante-el-dia-MtNEziYlm2U\",\"ja\"" +
                ":\"昼間灰色のコンクリートの床の上に立つ人々のグループ-MtNEziYlm2U\",\"fr\":\"groupe-de-personnes-debout-sur-le-sol-en-beton-gris-pendant-la-journee-MtNEziYlm2U\",\"it\":\"gruppo-di-persone-in-piedi-sul-pavimento-di-cemento-grigio-durante-il-giorno-MtNEziYlm2U\",\"ko\":\"낮-동안-회색-콘크리트-바닥에-서-있는-사람들의-그룹-MtNEziYlm2U\",\"de\"" +
                ":\"personengruppe-die-tagsuber-auf-grauem-betonboden-steht-MtNEziYlm2U\",\"pt\":\"grupo-de-pessoas-em-pe-no-chao-de-concreto-cinza-durante-o-dia-MtNEziYlm2U\"},\"created_at\":\"2020-10-10T11:10:29Z\",\"updated_at\":\"2024-07-26T12:46:31Z\",\"promoted_at\":null,\"width\":4838,\"height\":3456,\"color\":\"#d9d9f3\",\"blur_hash\"" +
                ":\"LSJtlGx]M#xY9ENfbas;4,bcS#a\$\",\"description\":\"Ongoing Protest to End Sars Brutality in Nigeria.\",\"alt_description\":\"group of people standing on gray concrete floor during daytime\",\"breadcrumbs\":[],\"urls\":{\"raw\":\"https://images.unsplash.com/photo-1602327850587-fc4b61215d07?ixid=M3wyNDI1MTl8MHwxfHNlYXJjaHwyfHxOaWdlcmlhfGVufDB8fHx8MTcyMjUxODIxOXww&ixlib=rb-4.0.3\",\"full\"" +
                ":\"https://images.unsplash.com/photo-1602327850587-fc4b61215d07?crop=entropy&cs=srgb&fm=jpg&ixid=M3wyNDI1MTl8MHwxfHNlYXJjaHwyfHxOaWdlcmlhfGVufDB8fHx8MTcyMjUxODIxOXww&ixlib=rb-4.0.3&q=85\",\"regular\":\"https://images.unsplash.com/photo-1602327850587-fc4b61215d07?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wyNDI1MTl8MHwxfHNlYXJjaHwyfHxOaWdlcmlhfGVufDB8fHx8MTcyMjUxODIxOXww&ixlib=rb-4.0.3&q=80&w=1080\",\"small\"" +
                ":\"https://images.unsplash.com/photo-1602327850587-fc4b61215d07?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wyNDI1MTl8MHwxfHNlYXJjaHwyfHxOaWdlcmlhfGVufDB8fHx8MTcyMjUxODIxOXww&ixlib=rb-4.0.3&q=80&w=400\",\"thumb\":\"https://images.unsplash.com/photo-1602327850587-fc4b61215d07?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wyNDI1MTl8MHwxfHNlYXJjaHwyfHxOaWdlcmlhfGVufDB8fHx8MTcyMjUxODIxOXww&ixlib=rb-4.0.3&q=80&w=200\",\"small_s3\"" +
                ":\"https://s3.us-west-2.amazonaws.com/images.unsplash.com/small/photo-1602327850587-fc4b61215d07\"},\"links\":{\"self\":\"https://api.unsplash.com/photos/group-of-people-standing-on-gray-concrete-floor-during-daytime-MtNEziYlm2U\",\"html\":\"https://unsplash.com/photos/group-of-people-standing-on-gray-concrete-floor-during-daytime-MtNEziYlm2U\",\"download\"" +
                ":\"https://unsplash.com/photos/MtNEziYlm2U/download?ixid=M3wyNDI1MTl8MHwxfHNlYXJjaHwyfHxOaWdlcmlhfGVufDB8fHx8MTcyMjUxODIxOXww\",\"download_location\":\"https://api.unsplash.com/photos/MtNEziYlm2U/download?ixid=M3wyNDI1MTl8MHwxfHNlYXJjaHwyfHxOaWdlcmlhfGVufDB8fHx8MTcyMjUxODIxOXww\"},\"likes\":27,\"liked_by_user\":false,\"current_user_collections\":[],\"sponsorship\"" +
                ":null,\"topic_submissions\":{\"current-events\":{\"status\":\"rejected\"},\"street-photography\":{\"status\":\"rejected\"}},\"asset_type\":\"photo\",\"user\":{\"id\":\"d2X4tWD_Xjs\",\"updated_at\":\"2024-06-20T18:06:29Z\",\"username\":\"magicconceptstudio\",\"name\":\"Tobi Oshinnaike\",\"first_name\"" +
                ":\"Tobi\",\"last_name\":\"Oshinnaike\",\"twitter_username\":\"Magicconceptst1\",\"portfolio_url\":\"https://unsplash.com/magicconcept\",\"bio\":\"• PORTRAIT \\r\\n• PRODUCT  • DOCUMENTARY Open for Collaboration  I rep \uD83C\uDDF3\uD83C\uDDEC\",\"location\":\"Lagos Nigeria\",\"links\":{\"self\":\"https://api.unsplash.com/users/magicconceptstudio\",\"html\"" +
                ":\"https://unsplash.com/@magicconceptstudio\",\"photos\":\"https://api.unsplash.com/users/magicconceptstudio/photos\",\"likes\":\"https://api.unsplash.com/users/magicconceptstudio/likes\",\"portfolio\":\"https://api.unsplash.com/users/magicconceptstudio/portfolio\",\"following\"" +
                ":\"https://api.unsplash.com/users/magicconceptstudio/following\",\"followers\":\"https://api.unsplash.com/users/magicconceptstudio/followers\"},\"profile_image\":{\"small\":\"https://images.unsplash.com/profile-1620192120692-3b6df822e182image?ixlib=rb-4.0.3&crop=faces&fit=crop&w=32&h=32\",\"medium\"" +
                ":\"https://images.unsplash.com/profile-1620192120692-3b6df822e182image?ixlib=rb-4.0.3&crop=faces&fit=crop&w=64&h=64\",\"large\":\"https://images.unsplash.com/profile-1620192120692-3b6df822e182image?ixlib=rb-4.0.3&crop=faces&fit=crop&w=128&h=128\"},\"instagram_username\"" +
                ":\"Magicconceptstudio\",\"total_collections\":3,\"total_likes\":78,\"total_photos\":64,\"total_promoted_photos\":0,\"total_illustrations\":0,\"total_promoted_illustrations\":0,\"accepted_tos\":true,\"for_hire\":true,\"social\"" +
                ":{\"instagram_username\":\"Magicconceptstudio\",\"portfolio_url\":\"https://unsplash.com/magicconcept\",\"twitter_username\":\"Magicconceptst1\",\"paypal_email\":null}},\"tags\":[{\"type\":\"search\",\"title\":\"nigeria\"}," +
                "{\"type\":\"search\",\"title\":\"lagos\"},{\"type\":\"search\",\"title\":\"protest\"}]}]}"

    val successfulImageResponse: String
        get() = "{\"id\":\"vzpEjttBI0M\",\"slug\":\"text-vzpEjttBI0M\",\"alternative_slugs\":{\"en\":\"text-vzpEjttBI0M\",\"es\"" +
                ":\"texto-vzpEjttBI0M\",\"ja\":\"文章-vzpEjttBI0M\",\"fr\":\"texte-vzpEjttBI0M\",\"it\":\"testo-vzpEjttBI0M\",\"ko\"" +
                ":\"문자-메시지-vzpEjttBI0M\",\"de\":\"text-vzpEjttBI0M\",\"pt\":\"texto-vzpEjttBI0M\"},\"created_at\":\"2020-10-10T11:10:29Z\",\"updated_at\"" +
                ":\"2024-07-26T09:54:31Z\",\"promoted_at\":null,\"width\":3329,\"height\":4661,\"color\":\"#262626\",\"blur_hash\"" +
                ":\"LDHBDE,^E1VttQ~Cr]s;I*Im-:Sw\",\"description\":\"On going Protest in Nigeria \uD83C\uDDF3\uD83C\uDDEC to End SARS killing,\",\"alt_description\"" +
                ":\"text\",\"breadcrumbs\":[],\"urls\":{\"raw\":\"https://images.unsplash.com/photo-1602327850706-eadbbd6a1522?ixid=M3wyNDI1MTl8MHwxfGFsbHx8fHx8fHx8fDE3MjI1MTk2Mzl8&ixlib=rb-4.0.3\",\"full\"" +
                ":\"https://images.unsplash.com/photo-1602327850706-eadbbd6a1522?crop=entropy&cs=srgb&fm=jpg&ixid=M3wyNDI1MTl8MHwxfGFsbHx8fHx8fHx8fDE3MjI1MTk2Mzl8&ixlib=rb-4.0.3&q=85\",\"regular\"" +
                ":\"https://images.unsplash.com/photo-1602327850706-eadbbd6a1522?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wyNDI1MTl8MHwxfGFsbHx8fHx8fHx8fDE3MjI1MTk2Mzl8&ixlib=rb-4.0.3&q=80&w=1080\",\"small\"" +
                ":\"https://images.unsplash.com/photo-1602327850706-eadbbd6a1522?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wyNDI1MTl8MHwxfGFsbHx8fHx8fHx8fDE3MjI1MTk2Mzl8&ixlib=rb-4.0.3&q=80&w=400\",\"thumb\"" +
                ":\"https://images.unsplash.com/photo-1602327850706-eadbbd6a1522?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wyNDI1MTl8MHwxfGFsbHx8fHx8fHx8fDE3MjI1MTk2Mzl8&ixlib=rb-4.0.3&q=80&w=200\",\"small_s3\"" +
                ":\"https://s3.us-west-2.amazonaws.com/images.unsplash.com/small/photo-1602327850706-eadbbd6a1522\"},\"links\":{\"self\":\"https://api.unsplash.com/photos/text-vzpEjttBI0M\",\"html\"" +
                ":\"https://unsplash.com/photos/text-vzpEjttBI0M\",\"download\":\"https://unsplash.com/photos/vzpEjttBI0M/download?ixid=M3wyNDI1MTl8MHwxfGFsbHx8fHx8fHx8fDE3MjI1MTk2Mzl8\",\"download_location\"" +
                ":\"https://api.unsplash.com/photos/vzpEjttBI0M/download?ixid=M3wyNDI1MTl8MHwxfGFsbHx8fHx8fHx8fDE3MjI1MTk2Mzl8\"},\"likes\":22,\"liked_by_user\":false,\"current_user_collections\":[],\"sponsorship\"" +
                ":null,\"topic_submissions\":{\"current-events\":{\"status\":\"rejected\"},\"street-photography\":{\"status\":\"rejected\"}},\"asset_type\":\"photo\",\"user\":{\"id\":\"d2X4tWD_Xjs\",\"updated_at\":\"2024-06-20T18:06:29Z\",\"username\"" +
                ":\"magicconceptstudio\",\"name\":\"Tobi Oshinnaike\",\"first_name\":\"Tobi\",\"last_name\":\"Oshinnaike\",\"twitter_username\":\"Magicconceptst1\",\"portfolio_url\":\"https://unsplash.com/magicconcept\",\"bio\"" +
                ":\"• PORTRAIT \\r\\n• PRODUCT  • DOCUMENTARY Open for Collaboration  I rep \uD83C\uDDF3\uD83C\uDDEC\",\"location\":\"Lagos Nigeria\",\"links\":{\"self\":\"https://api.unsplash.com/users/magicconceptstudio\",\"html\"" +
                ":\"https://unsplash.com/@magicconceptstudio\",\"photos\":\"https://api.unsplash.com/users/magicconceptstudio/photos\",\"likes\":\"https://api.unsplash.com/users/magicconceptstudio/likes\",\"portfolio\"" +
                ":\"https://api.unsplash.com/users/magicconceptstudio/portfolio\",\"following\":\"https://api.unsplash.com/users/magicconceptstudio/following\",\"followers\":\"https://api.unsplash.com/users/magicconceptstudio/followers\"},\"profile_image\"" +
                ":{\"small\":\"https://images.unsplash.com/profile-1620192120692-3b6df822e182image?ixlib=rb-4.0.3&crop=faces&fit=crop&w=32&h=32\",\"medium\":\"https://images.unsplash.com/profile-1620192120692-3b6df822e182image?ixlib=rb-4.0.3&crop=faces&fit=crop&w=64&h=64\",\"large\"" +
                ":\"https://images.unsplash.com/profile-1620192120692-3b6df822e182image?ixlib=rb-4.0.3&crop=faces&fit=crop&w=128&h=128\"},\"instagram_username\":\"Magicconceptstudio\",\"total_collections\":3,\"total_likes\":78,\"total_photos\":64,\"total_promoted_photos\":0,\"total_illustrations\"" +
                ":0,\"total_promoted_illustrations\":0,\"accepted_tos\":true,\"for_hire\":true,\"social\":{\"instagram_username\":\"Magicconceptstudio\",\"portfolio_url\":\"https://unsplash.com/magicconcept\",\"twitter_username\":\"Magicconceptst1\",\"paypal_email\":null}},\"location\"" +
                ":{\"name\":\"Oba Ayangbure Palace Ikorodu Oba Palace., Ikorodu Rd, Lagos, Nigeria\",\"city\":\"Lagos\",\"country\":\"Nigeria\",\"position\":{\"latitude\":6.601835,\"longitude\":3.516682}},\"meta\":{\"index\":true},\"public_domain\":false,\"views\":298817,\"downloads\":2795,\"topics\":[]}"

    val authorizationError: String
        get() = "{\"errors\":[\"OAuth error: The access token is invalid\"]}"
}