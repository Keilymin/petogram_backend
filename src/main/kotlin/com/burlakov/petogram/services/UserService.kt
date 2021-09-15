package com.burlakov.petogram.services

import com.burlakov.petogram.models.Answer
import com.burlakov.petogram.models.User
import com.burlakov.petogram.repositories.UserRepository
import com.lambdaworks.crypto.SCryptUtil
import net.bytebuddy.utility.RandomString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import javax.mail.internet.MimeMessage


@Service
class UserService : IUserService {
    @Autowired
    lateinit var repository: UserRepository

    @Autowired
    lateinit var mailSender: JavaMailSender

    override fun saveUser(user: User, siteURL: String) {
        val randomCode: String = RandomString.make(64)
        user.verificationCode = randomCode
        sendVerificationEmail(user, siteURL)
        repository.save(user)
    }

    override fun existByEmail(email: String): Boolean {
        return repository.findByEmail(email) != null
    }

    override fun forgotPassword(email: String, siteURL: String): Answer {
        val user = repository.findByEmail(email)
        if (user != null) {
            sendPasswordRestore(user, siteURL)
            return Answer("Message send", true)
        } else return Answer("Not registered", false)
    }

    override fun checkUser(user: User): Answer {
        val u = repository.findById(user.id!!)
            return if (user.password == u.get().password) {
                Answer("OK", true, u.get())
            } else Answer("Password is decline", false)
    }

    override fun logIn(user: User): Answer {
        val u = repository.findByEmail(user.email)
        return if (u != null) {
            if (SCryptUtil.check(user.password, u.password)) {
                if (u.active) {
                    Answer("OK", true, u)
                } else {
                    Answer("Account is not activate", false)
                }
            } else Answer("Wrong data", false)
        } else Answer("Not registered", false)
    }

    override fun sendVerificationEmail(user: User, siteURL: String) {
        val verifyURL: String = siteURL + "/verify?code=" + user.verificationCode

        val toAddress: String = user.email
        val fromAddress = "noreply.petogram@gmail.com"
        val senderName = "Petogram App"
        val subject = "Please verify your registration"
        val content = ("Dear ${user.email},<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"$verifyURL\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Your Petogram.")

        val message: MimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message)

        helper.setFrom(fromAddress, senderName)
        helper.setTo(toAddress)
        helper.setSubject(subject)

        helper.setText(content, true)

        mailSender.send(message)
    }

    override fun sendPasswordRestore(user: User, siteURL: String) {
        val restoreURL: String = siteURL + "/restore?email=" + user.email

        val name: String = if (user.username != null) {
            user.username!!
        } else
            user.email

        val toAddress: String = user.email

        val fromAddress = "noreply.petogram@gmail.com"
        val senderName = "Petogram App"
        val subject = "Please restore your password"
        val content = ("Dear ${name},<br>"
                + "Please click the link below to restore your password:<br>"
                + "<h3><a href=\"$restoreURL\" target=\"_self\">RESTORE</a></h3>"
                + "Thank you,<br>"
                + "Your Petogram.")

        val message: MimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message)

        helper.setFrom(fromAddress, senderName)
        helper.setTo(toAddress)
        helper.setSubject(subject)

        helper.setText(content, true)

        mailSender.send(message)
    }

    override fun updateUsername(id: String, username: String): Answer {
        if (repository.findByUsername(username) == null) {
            val user = repository.findById(id.toLong()).get()
            user.username = username
            repository.save(user)
            return Answer("Ok", true)
        } else return Answer("Username exists", false)
    }

    override fun updateAvatar(id: String, avatar: String) {
        val user = repository.findById(id.toLong()).get()
        user.avatar = avatar
        repository.save(user)
    }

    fun verify(verificationCode: String): Boolean {
        val user: User? = repository.findByVerificationCode(verificationCode)
        return if (user == null || user.active) {
            false
        } else {
            user.verificationCode = null
            user.active = true
            repository.save(user)
            true
        }
    }

    fun restore(email: String): Boolean {
        val user: User? = repository.findByEmail(email)
        return if (user == null) {
            false
        } else {
            val pas = RandomString.make(8)
            user.password = pas
            sendPas(user)
            user.password = SCryptUtil.scrypt(user.password, 16, 8, 1)
            repository.save(user)
            true
        }
    }

    fun sendPas(user: User) {

        val name: String = if (user.username != null) {
            user.username!!
        } else
            user.email

        val toAddress: String = user.email

        val fromAddress = "noreply.petogram@gmail.com"
        val senderName = "Petogram App"
        val subject = "Your new password"
        val content = ("Dear ${name},<br>"
                + "Your new password is:<br>"
                + "<h3>${user.password}</h3>"
                + "<br>Please change your password after login<br>"
                + "Thank you,<br>"
                + "Your Petogram.")

        val message: MimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message)

        helper.setFrom(fromAddress, senderName)
        helper.setTo(toAddress)
        helper.setSubject(subject)

        helper.setText(content, true)

        mailSender.send(message)
    }

}