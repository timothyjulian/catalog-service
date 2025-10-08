package me.timothy.catalog.service.util

import me.timothy.catalog.service.entity.Course

fun courseEntityList() = listOf(
    Course(
        null,
        "Build RestFul APis using SpringBoot and Kotlin", "Development"
    ),
    Course(null,
        "Build Reactive Microservices using Spring WebFlux/SpringBoot", "Development"
        ,
    ),
    Course(null,
        "Wiremock for Java Developers", "Development" ,
    )
)
