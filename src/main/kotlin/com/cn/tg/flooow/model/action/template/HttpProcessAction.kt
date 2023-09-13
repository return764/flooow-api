package com.cn.tg.flooow.model.action.template

import com.cn.tg.flooow.exceptions.TaskRuntimeException
import com.cn.tg.flooow.exceptions.TaskValidationException
import com.cn.tg.flooow.model.action.AbstractAction
import com.cn.tg.flooow.model.action.Action
import com.cn.tg.flooow.model.action.annotation.ActionMarker
import com.cn.tg.flooow.model.action.annotation.ActionOption
import com.cn.tg.flooow.model.action.template.enums.HttpMethod
import okhttp3.Headers.Companion.toHeaders
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


@ActionMarker(type = "process", name = "http", shape = "process", label="HTTP Request")
class HttpProcessAction: AbstractAction(), Action {

    @ActionOption(name = "method", defaultValue = "GET")
    private lateinit var method: HttpMethod

    @ActionOption(name = "headers")
    private lateinit var headers: Map<String, String>

    @ActionOption(name = "url")
    private lateinit var url: String

    override fun validate() {
        if (!HttpMethod.values().toSet().contains(method)) {
            throw TaskValidationException("not support http method")
        }

        if (!url.startsWith("http")) {
            throw TaskValidationException("url is not start with http")
        }
    }

    override fun run() {
        val client = OkHttpClient()
        val requestBuilder = Request.Builder().headers(headers.toHeaders()).url(url)
        if (method.name == "GET") {
            requestBuilder.get()
        } else if (method.name == "POST") {
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
                throw TaskRuntimeException(body ?: "")
            }
        } catch (e: RuntimeException) {
            throw TaskRuntimeException("error: ${e.message}")
        } catch (e: IOException) {
            throw TaskRuntimeException("connect error: ${e.message}")
        }
    }
}
