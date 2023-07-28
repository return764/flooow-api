package com.cn.tg.flooow.controller

import com.cn.tg.flooow.controller.response.R
import com.cn.tg.flooow.exceptions.TaskException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionController {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(TaskException::class)
    fun handleTaskException(e: TaskException): R {
        return R.failed(e.message ?: "something went wrong.")
    }
}
