package com.burlakov.petogram

import com.burlakov.petogram.models.Answer
import com.burlakov.petogram.models.User
import com.burlakov.petogram.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class RegisterController {

    @Autowired
    lateinit var service: UserService

    @PostMapping("/singUp", produces = ["application/json"])
    fun singUn(@RequestBody user: User): Answer {
        return if (service.existByEmail(user.email)) {
            Answer("Already exist", false)
        } else {
            service.saveUser(user)
            Answer("Saved", true)
        }
    }
    @PostMapping("/logIn", produces = ["application/json"])
    fun logIn(@RequestBody user: User): Answer {
        return service.logIn(user)
    }

//    @GetMapping("/", produces = ["application/json"])
//    fun singUn(): User {
//        //Thread.sleep(10000)
//        return User("123","333")
//    }

}