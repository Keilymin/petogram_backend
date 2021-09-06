package com.burlakov.petogram.services

import com.burlakov.petogram.models.Answer
import com.burlakov.petogram.models.User

interface IUserService {
    fun saveUser(user: User, siteURL : String)
    fun existByEmail(email: String): Boolean
    fun logIn(user: User): Answer
    fun sendVerificationEmail(user: User,  siteURL:String)
    fun checkUser(user: User): Answer
    fun forgotPassword(email: String, siteURL: String): Answer
    fun sendPasswordRestore(user: User, siteURL: String)
}