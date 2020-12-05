package com.a.domainmodule.inputValidation

import android.util.Log

class ValidateInput {

    fun checkEmailValidation(email: String): Boolean {
        if (email.isEmpty()) {
            Log.i("baby", "Username can not be empty")
            return false
        } else if (email.contains('@') ||
            email.contains('*') ||
            email.contains('/') ||
            email.contains('-') ||
            email.contains('+') ||
            email.contains('(') ||
            email.contains(')') ||
            email.contains('#') ||
            email.contains('$') ||
            email.contains('^') ||
            email.contains('=') ||
            email.contains('!') ||
            email.contains(':') ||
            email.contains('\'') ||
            email.contains('\\') ||
            email.contains('\"') ||
            email.contains(';') ||
            email.contains('{') ||
            email.contains('}') ||
            email.contains('>') ||
            email.contains('<') ||
            email.contains('?') ||
            email.contains(',')
        ) {
            Log.i("baby", "Username is invalid")
            return false
        } else if (email.length > 20) {
            Log.i("baby", "Username should be maximum 20 characters")
            return false
        }
        Log.i("baby", "Username Confirmed")
        return true
    }

    fun checkPasswordValidation(password: String): Boolean {
        if (password.isEmpty()) {
            Log.i("baby", "Password can not be empty")
            return false
        } else if (password.length < 6) {
            Log.i("baby", "Enter a password with at least 6 characters")
            return false
        }
        Log.i("baby", "password confirmed")
        return true
    }

    fun checkTheSamePassword(password: String, repeatPassword: String): Boolean {
        return if (password == repeatPassword) true
        else {
            Log.i("baby", "passwords don't match")
            false
        }
    }

    fun checkNameValidation(name: String): Boolean {
        if (name.isEmpty()) {
            Log.i("baby", "Name can not be empty")
            return false
        } else if (name.length < 4) {
            Log.i("baby", "Enter a name with at least 4 characters")
            return false
        }
        return true
    }

}