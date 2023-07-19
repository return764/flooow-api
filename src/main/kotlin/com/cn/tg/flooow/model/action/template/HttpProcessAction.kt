package com.cn.tg.flooow.model.action.template

import com.cn.tg.flooow.exceptions.TaskException
import com.cn.tg.flooow.model.action.AbstractAction
import com.cn.tg.flooow.model.action.Action
import com.cn.tg.flooow.model.action.annotation.ActionMarker
import com.cn.tg.flooow.model.action.annotation.ActionOption
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


@ActionMarker(type = "process", name = "http", shape = "process", label="HTTP Request")
class HttpProcessAction: AbstractAction(), Action {

    @ActionOption(name = "method", defaultValue = "GET")
    private lateinit var method: String

    @ActionOption(name = "url", defaultValue = "")
    private lateinit var url: String

    override fun run() {
        val client = OkHttpClient()
        val requestBuilder = Request.Builder().url(url)
        if (method == "GET") {
            requestBuilder.get()
        } else if (method == "POST") {
            requestBuilder.post("".toRequestBody())
        }
        try {
            val response = client.newCall(requestBuilder.build()).execute()
            val body = response.body?.string()
            if (response.isSuccessful) {
                returnValue(body)
                return
            }
            if (response.code >= 400) {
                throw TaskException(body ?: "")
            }
        } catch (e: IOException) {
            throw TaskException("connect error: ${e.message}")
        }
    }
}
