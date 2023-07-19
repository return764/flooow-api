package com.cn.tg.flooow.service

import com.cn.tg.flooow.enums.ReturnType
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class MessageHandler(
    private val template: SimpMessagingTemplate
) {

    companion object {
        const val RETURN_TYPE = "return-type"
    }

    fun sendWithHeader(destination: String, payload: Any?, header: Map<String, Any>) {
        template.convertAndSend(destination, payload ?: "", header)
    }

    fun builder(): MessageBuilder {
        return MessageBuilder(this)
    }


    class MessageBuilder(
        private val handler: MessageHandler,
    ) {
        private lateinit var destination: String
        private val header: MutableMap<String, Any> = mutableMapOf()
        private var payload: Any? = null

        fun header(key: String, value: Any): MessageBuilder {
            this.header[key] = value
            return this
        }

        fun payload(value: Any?): MessageBuilder {
            this.payload = value
            return this
        }

        fun destination(value: String): MessageBuilder {
            this.destination = value
            return this
        }

        fun returnType(type: ReturnType): MessageBuilder {
            return header(RETURN_TYPE, type)
        }

        fun send() {
            handler.sendWithHeader(destination, payload, header)
        }
    }
}
