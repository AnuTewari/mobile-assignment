package org.takingroot.assignment.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.takingroot.assignment.models.Survey
import org.takingroot.assignment.utils.validator.BirthDate
import org.takingroot.assignment.utils.validator.Email
import org.takingroot.assignment.utils.validator.NotEmpty
import org.takingroot.assignment.viewmodels.SurveyFormState
import org.takingroot.assignment.viewmodels.SurveyViewModel
import java.util.*

fun submitData(surveyData: Map<String, Any>, viewModel: SurveyViewModel) {
    val survey = Survey(
        name = "user",
        payload = surveyData
    )

    viewModel.save(survey)
}
@Composable
fun SurveyForm(state: SurveyFormState, formFields: List<FormField>){
    state.formFields = formFields

    Column {
        formFields.forEach {
            it.FieldContent()
        }
    }
}

@Composable
fun SurveyFormDialog(viewModel: SurveyViewModel, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = {
            onDismiss()
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp)
        ) {
            val formState by remember { mutableStateOf(SurveyFormState()) }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("User Form", style = MaterialTheme.typography.titleLarge)

                SurveyForm(
                    state = formState,
                    formFields = listOf(
                        FormField(
                            fieldName = "first_name",
                            fieldLabel = "First Name",
                            fieldValidators = listOf(
                                NotEmpty()
                            )
                        ),
                        FormField(
                            fieldName = "last_name",
                            fieldLabel = "Last Name",
                            fieldValidators = listOf(
                                NotEmpty()
                            )
                        ),
                        FormField(
                            fieldName = "email",
                            fieldLabel = "Email Address",
                            fieldValidators = listOf(
                                NotEmpty(), Email()
                            )
                        ),
                        FormField(
                            fieldName = "birth_date",
                            fieldLabel = "DOB(YYYY-MM-DD)",
                            fieldValidators = listOf(
                                NotEmpty(), BirthDate()
                            )
                        )
                    )
                )
                Button(onClick = {
                    if (formState.validate()) {
                        var surveyData = formState.getFormData()
                        val uuid = UUID.randomUUID()
                        surveyData += mapOf("id" to uuid)
                        submitData(surveyData, viewModel)
                        onDismiss()
                    }
                }) {
                    Text(text = "Submit")
                }
            }
        }
    }
}