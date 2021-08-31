package com.burlakov.petogram

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PetogramApplication

fun main(args: Array<String>) {
	runApplication<PetogramApplication>(*args)

}

