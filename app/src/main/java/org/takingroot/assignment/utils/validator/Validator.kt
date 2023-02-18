package org.takingroot.assignment.utils.validator

private const val EMPTY_ERROR = "Field cannot be empty"
private const val INVALID_EMAIL = "Email address invalid"
private const val INVALID_AGE = "Age must be valid YYYY-MM-DD and 18+"
sealed interface Validator
open class NotEmpty(var message: String = EMPTY_ERROR): Validator
open class Email(var message: String = INVALID_EMAIL): Validator
open class BirthDate(var message: String = INVALID_AGE): Validator