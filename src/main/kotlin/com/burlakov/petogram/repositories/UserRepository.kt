package com.burlakov.petogram.repositories

import com.burlakov.petogram.models.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User, Long> {
    fun findByEmail(email: String) : User?
    fun findByVerificationCode(code: String): User?
}