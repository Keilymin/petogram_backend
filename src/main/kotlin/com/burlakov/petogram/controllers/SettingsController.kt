package com.burlakov.petogram.controllers

import com.burlakov.petogram.models.Answer
import com.burlakov.petogram.models.User
import com.burlakov.petogram.services.UserService
import com.burlakov.petogram.utils.FileUploadUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.query.Param
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletRequest

@RestController
class SettingsController {

    @Autowired
    lateinit var service: UserService

    @PostMapping("/changeUsername", produces = ["application/json"])
    fun changeUsername(@RequestBody user: User): Answer {
        return service.updateUsername(user.id.toString(), user.username!!)
    }

    @PostMapping("/changePassword", produces = ["application/json"])
    fun changePassword(@RequestBody user: User): Answer {
        return service.updatePassword(user.id.toString(), user.password, user.verificationCode!!)
    }

    @PostMapping("/changeEmail", produces = ["application/json"])
    fun changeEmail(@RequestBody user: User, request: HttpServletRequest): Answer {
        return service.updateEmail(user.id.toString(), user.email, getSiteURL(request))
    }

    @PostMapping("/changeAvatar", produces = ["application/json"])
    fun changeAvatar(@RequestParam("userId") userId: String, @RequestParam("image") multipartFile: MultipartFile): Answer {
        val fileName: String = StringUtils.cleanPath(multipartFile.originalFilename!!)
        val uploadDir = "images/$userId"
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile)
        service.updateAvatar(userId, fileName)

        return service.updateAvatar(userId, fileName)
    }

    private fun getSiteURL(request: HttpServletRequest): String {
        val siteURL = request.requestURL.toString()
        return siteURL.replace(request.servletPath, "")
    }

    @GetMapping("/backemail")
    fun backemail(@Param("code") code: String): String? {
        val strings = code.split("/")
        service.backemail(strings[0], strings[1])
        return "<div align=\"center\">\n" +
                "<h3>Congratulations, your email has been restored.</h3>\n" +
                "</div>"
    }
}