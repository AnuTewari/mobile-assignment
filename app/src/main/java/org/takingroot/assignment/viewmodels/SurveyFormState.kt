package org.takingroot.assignment.viewmodels

import org.takingroot.assignment.components.FormField

class SurveyFormState {
    var formFields: List<FormField> = listOf()

    fun validate(): Boolean {
        var valid = true
        for (field in formFields) {
            if (!field.validate()) {
                valid = false
                break
            }
        }
        return valid
    }

    fun getFormData(): Map<String, Any> = formFields.associate { it.fieldName to it.fieldText }
}