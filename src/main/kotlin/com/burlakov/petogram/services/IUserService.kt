package com.burlakov.petogram.services

import com.burlakov.petogram.models.Answer
import com.burlakov.petogram.models.User

interface IUserService {
    fun saveUser(user: User, siteURL: String)
    fun existByEmail(email: String): Boolean
    fun logIn(user: User): Answer
    fun sendVerificationEmail(user: User, siteURL: String)
    fun checkUser(user: User): Answer
    fun forgotPassword(email: String, siteURL: String): Answer
    fun sendPasswordRestore(user: User, siteURL: String)
    fun updateUsername(id: String, username: String): Answer
    fun updateAvatar(id: String, avatar: String): Answer
    fun updatePassword(id: String, OldPassword: String, NewPassword: String): Answer
    fun updateEmail(id: String, email: String, siteURL: String): Answer
    fun backemail(id: String, email: String)

}