package com.vestis.data.profile.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDTO(
    @SerialName(value = "id") val id: Int,
    @SerialName(value = "email") val email: String,
    @SerialName(value = "username") val username: String,
    @SerialName(value = "name") val name: Name,
    @SerialName(value = "address") val address: Address,
    @SerialName(value = "phone") val phone: String
) {
    @Serializable
    data class Name(
        @SerialName(value = "firstname") val firstName: String,
        @SerialName(value = "lastname") val lastName: String
    )

    @Serializable
    data class Address(
        @SerialName(value = "city") val city: String,
        @SerialName(value = "street") val street: String,
        @SerialName(value = "number") val number: Int,
        @SerialName(value = "zipcode") val zipcode: String,
        @SerialName(value = "geolocation") val geolocation: Geolocation
    ) {
        @Serializable
        data class Geolocation(
            @SerialName(value = "lat") val lat: String,
            @SerialName(value = "long") val long: String
        )
    }
}