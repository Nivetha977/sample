package com.example.sample_app.common

/**
*Created by Nivetha S on 22-11-2021.
*/

fun String.isValidPhoneNumber(): Boolean {
    val regex = "^([1-9]{0}?[0-9]{1,9}).{9}\$" // Todo Make regex better to not allow start with 0
    if (this.matches(Regex(regex))) {
        return true
    }
    return false
}