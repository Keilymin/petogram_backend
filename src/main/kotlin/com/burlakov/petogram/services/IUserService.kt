package com.burlakov.petogram.services

import com.burlakov.petogram.models.Answer
import com.burlakov.petogram.models.User

interface IUserService {
    fun saveUser(user: User)
    fun existByEmail(email: String): Boolean
    fun logIn(user: User): Answer
}