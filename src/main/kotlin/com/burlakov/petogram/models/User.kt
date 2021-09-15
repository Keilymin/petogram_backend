package com.burlakov.petogram.models

import javax.persistence.*

@Entity
@Table(name = "users")
data class User(@Id
                @GeneratedValue(strategy = GenerationType.IDENTITY)
                var id: Long?,
                var username: String?,
                var email: String,
                var password: String,
                @Column(name = "verification_code", length = 64)
                var verificationCode: String?,
                var active: Boolean,
                var avatar: String?) {
    constructor(email: String, password: String) : this(null, null, email, password, null, false, null)

    constructor() : this(null, null, "error", "error", null, false, null) {
    }

    fun equals(other: User): Boolean {
        return id == other.id &&
                username == other.username &&
                email == other.email &&
                password == other.password &&
                avatar == other.avatar
    }
}