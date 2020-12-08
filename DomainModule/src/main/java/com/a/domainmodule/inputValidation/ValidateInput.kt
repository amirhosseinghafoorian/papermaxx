package com.a.domainmodule.inputValidation

class ValidateInput {

    fun checkEmailValidation(email: String): String {
        if (email.isEmpty()) {
            return "Username can not be empty"
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
            return "Username is invalid"
        } else if (email.length > 20) {
            return "Username should be maximum 20 characters"
        }
        return ""
    }

    fun checkPasswordValidation(password: String): String {
        if (password.isEmpty()) {
            return "Password can not be empty"
        } else if (password.length < 6) {
            return "Enter a password with at least 6 characters"
        }
        return ""
    }

    fun checkTheSamePassword(password: String, repeatPassword: String): String {
        return if (password == repeatPassword) ""
        else {
            "passwords don't match"
        }
    }

    fun checkNameValidation(name: String): String {
        if (name.isEmpty()) {
            return "Name can not be empty"
        } else if (name.length < 4) {
            return "Enter a name with at least 4 characters"
        }
        return ""
    }

}