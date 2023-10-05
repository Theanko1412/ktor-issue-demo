package demo.ktor

import io.ktor.http.Headers
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.ResponseHeaders
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.util.toMap

/* demo of old behaviour */
val headersNames = mutableListOf<String>()
val headerValues = mutableListOf<String>()

fun main(args: Array<String>) {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
//    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
//        .start(wait = true)
}

fun Application.module() {
    routing {
        get("/headersIssueTest") {
            call.response.headers.append("Test-Header", "Test-Value")
            println(call.response.headers.allValues().toMap())
            call.response.headers.append("Test-Header", "Test-Value2")
            println(call.response.headers.allValues().toMap())
            call.respond(HttpStatusCode.OK)
        }

        get("/headersSolutionTest") {
            call.response.headers.append("Test-Header", "Test-Value")
            //manually adding headers to private list for demo
            headersNames.add("Test-Header")
            headerValues.add("Test-Value")
            println(call.response.headers.allValuesFixed().toMap())
            call.response.headers.append("Test-Header", "Test-Value2")
            headersNames.add("Test-Header")
            headerValues.add("Test-Value2")
            println(call.response.headers.allValuesFixed().toMap())
            call.respond(HttpStatusCode.OK)
        }
    }
}

/*
mocking old behaviour of allValues() function
change commit: https://github.com/ktorio/ktor/commit/106aecfc50903afa8a2389a2bea7587809c167d8
*/
fun ResponseHeaders.allValuesFixed(): Headers = Headers.build {
    headersNames.forEach {
        appendAll(it, getEngineHeaderValuesFixed(it))
    }
}

fun ResponseHeaders.getEngineHeaderValuesFixed(name: String): List<String> = headersNames.indices
    .filter { headersNames[it].equals(name, ignoreCase = true) }
    .map { headerValues[it] }