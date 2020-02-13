package com.speedtest.api

import com.google.gson.Gson
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/speedtest")
class SpeedTestResource(val pubSub: PubSubTemplate) {

    val gson = Gson()

    @PostMapping
    fun publishResult(@RequestBody result: SpeedTestResult) {
        println(result)
        pubSub.publish("speedtest", gson.toJson(result))
    }

    data class SpeedTestResult(val user: String, val device: Int, val timestamp: Long, val data: SpeedTestResultData)

    data class SpeedTestResultData(val speeds: SpeedTestSpeeds, val client: SpeedTestClient, val server: SpeedTestServer)

    data class SpeedTestSpeeds(val download: Int, val upload: Int)

    data class SpeedTestClient(val ip: String, val lat: Double, val lon: Double, val isp: String, val country: String)

    data class SpeedTestServer(
            val host: String, val lat: Double, val lon: Double, val country: String, val distance: Int, val ping: Int, val id: String)
}