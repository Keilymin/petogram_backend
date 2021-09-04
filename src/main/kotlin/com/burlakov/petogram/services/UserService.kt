package com.burlakov.petogram.services

import com.burlakov.petogram.models.Answer
import com.burlakov.petogram.models.User
import com.burlakov.petogram.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService : IUserService {
    @Autowired
    lateinit var repository: UserRepository

    override fun saveUser(user: User) {
        repository.save(user)
    }

    override fun existByEmail(email: String): Boolean {
        return repository.findByEmail(email) != null
    }

    override fun logIn(user: User): Answer {
        val u = repository.findByEmail(user.email)
        return if (u != null) {
            if (user.password == u.password) {
                Answer("OK", true)
            } else Answer("Wrong data", false)
        } else Answer("Not registered", false)
    }

}