package org.takingroot.assignment.components

import android.util.Patterns
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.takingroot.assignment.utils.validator.BirthDate
import org.takingroot.assignment.utils.validator.Email
import org.takingroot.assignment.utils.validator.NotEmpty
import org.takingroot.assignment.utils.validator.Validator
import java.time.LocalDate
import java.time.Period

class FormField(val fieldName: String, val fieldLabel: String, val fieldValidators: List<Validator>) {
    var fieldText: String by mutableStateOf("")
    var label: String by mutableStateOf(fieldLabel)
    var containsError: Boolean by mutableStateOf(false)

    private fun displayError(fieldError: String) {
        containsError = true
        label = fieldError
    }

    private fun removeError() {
        containsError = false
        label = fieldLabel
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun FieldContent() {
        OutlinedTextField(
            value = fieldText,
            isError = containsError,
            label = {Text(text = label)},
            modifier = Modifier.padding(10.dp),
            onValueChange = { value ->
                removeError()
                fieldText = value
            }
        )
    }

    fun validate(): Boolean {
        return fieldValidators.map {
            when (it){
                is Email -> {
                    if (!Patterns.EMAIL_ADDRESS.matcher(fieldText).matches()) {
                        displayError(it.message)
                        return@map false
                    }
                    true
                }
                is NotEmpty -> {
                    if (fieldText.isEmpty()) {
                        displayError(it.message)
                        return@map false
                    }
                    true
                }
                is BirthDate -> {
                    val regex = "((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])"
                    if (!fieldText.matches(regex.toRegex())){
                        displayError(it.message)
                        return@map false
                    }
                    val birthDate = LocalDate.parse(fieldText)
                    val curDate = LocalDate.now()
                    val yearDiff = Period.between(birthDate, curDate).years

                    if (yearDiff < 18) {
                        displayError(it.message)
                        return@map false
                    }
                    true
                }
            }
        }.all { it }
    }
}