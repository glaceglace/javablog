package com.glaceglace.blog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GlaceBlogMainApplication

fun main(args: Array<String>) {
    runApplication<GlaceBlogMainApplication>(*args)
}