package com.vestis.domain.profile.model

data class ProfileModel(
    val id: Int,
    val email: String,
    val username: String,
    val name: Name
) {
    data class Name(
        val firstName: String,
        val lastName: String
    )
}
