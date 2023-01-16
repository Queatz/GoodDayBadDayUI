package api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.utils.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

//private const val baseUrl = "http://0.0.0.0:9090"
private const val baseUrl = "https://api.gooddaybad.day"

private val applicationJson = ContentType.Application.Json
private val http = HttpClient(Js) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
        })
    }
    expectSuccess = true
}

val api = Api()

@Serializable
data class MePostBody(val password: String)

class Api {

    var password: String? = null

    suspend fun packs(): List<PromptPack> = get("packs")

    suspend fun me(password: String): Person = post("connect", MePostBody(password))

    suspend fun updateMe(person: Person): Person = post("me", person)

    suspend fun pack(id: String): PromptPack = get("packs/$id")

    suspend fun myPacks(): List<PromptPack> = get("me/packs")

    suspend fun createPack(): PromptPack = post("packs")

    suspend fun deletePack(promptPack: String): HttpStatusCode = post("packs/$promptPack/delete")

    suspend fun updatePack(promptPack: PromptPack): HttpStatusCode = post("packs/${promptPack.id}", promptPack)

    private suspend inline fun <reified T> get(path: String) = http.get("$baseUrl/$path") {
        headers {
            password?.let { bearerAuth(it) }
        }
    }.body<T>()

    private suspend inline fun <reified T> post(path: String) = post<T, EmptyContent>(path, EmptyContent)

    private suspend inline fun <reified T, reified B> post(path: String, body: B?) = http.post("$baseUrl/$path") {
        setBody(body)
        contentType(applicationJson)
        headers {
            password?.let { bearerAuth(it) }
        }
    }.body<T>()
}
