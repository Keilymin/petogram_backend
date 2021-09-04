package com.burlakov.petogram.models

import javax.persistence.*

@Entity
@Table(name = "users")
data class User(@Id
                @GeneratedValue(strategy = GenerationType.IDENTITY)
                var id: Long?,
                var username: String?,
                var email: String,
                var password: String)
{
    constructor(email: String, password: String) : this(null, null, email, password)

    constructor() : this(null, null,"error","error") {
    }
}