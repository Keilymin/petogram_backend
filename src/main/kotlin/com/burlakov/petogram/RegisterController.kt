package com.burlakov.petogram

import com.burlakov.petogram.models.Answer
import com.burlakov.petogram.models.User
import com.burlakov.petogram.services.UserService
import com.lambdaworks.crypto.SCryptUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.query.Param
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest


@RestController
class RegisterController {

    @Autowired
    lateinit var service: UserService

    @PostMapping("/singUp", produces = ["application/json"])
    fun singUp(@RequestBody user: User, request: HttpServletRequest): Answer {
        return if (service.existByEmail(user.email)) {
            Answer("Already exist", false)
        } else {

            user.password = SCryptUtil.scrypt(user.password, 16, 8, 1)
            service.saveUser(user, getSiteURL(request))
            Answer("Saved", true)

        }
    }

    private fun getSiteURL(request: HttpServletRequest): String {
        val siteURL = request.requestURL.toString()
        return siteURL.replace(request.servletPath, "")
    }

    @PostMapping("/logIn", produces = ["application/json"])
    fun logIn(@RequestBody user: User): Answer {
        return service.logIn(user)
    }

    @PostMapping("/checkUser", produces = ["application/json"])
    fun checkUser(@RequestBody user: User): Answer {
        return service.checkUser(user)
    }

    @PostMapping("/forgotPassword", produces = ["application/json"])
    fun forgotPassword(@RequestBody email: String, request: HttpServletRequest): Answer {

        val newEmail = email.replace("\"", "")
        print(newEmail)

        return service.forgotPassword(newEmail, getSiteURL(request))
    }

    @GetMapping("/verify")
    fun verifyUser(@Param("code") code: String): String? {
        return if (service.verify(code)) {
            "<div align=\"center\">\n" +
                    "<h3>Congratulations, your account has been verified.</h3>\n" +
                    "</div>"
        } else {
            "<div align=\"center\">\n" +
                    "<h3>Sorry, we could not verify account. It maybe already verified,\n" +
                    " or verification code is incorrect.</h3>\n" +
                    "</div>"
        }
    }

    @GetMapping("/restore")
    fun restore(@Param("email") email: String): String? {
        return if (service.restore(email)) {
            "<div align=\"center\">\n" +
                    "<h3>Congratulations, check your email to see new password.</h3>\n" +
                    "</div>"
        } else {
            "<div align=\"center\">\n" +
                    "<h3>Sorry, we could not restore password. Maybe you change,\n" +
                    " your email.</h3>\n" +
                    "</div>"
        }
    }
}