package com.cn.tg.flooow

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class FlooowApplication: ApplicationRunner {
	override fun run(args: ApplicationArguments?) {
		println(1234)
	}
}

fun main(args: Array<String>) {
	runApplication<FlooowApplication>(*args)
}
