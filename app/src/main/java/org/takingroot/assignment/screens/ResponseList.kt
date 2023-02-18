package org.takingroot.assignment.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import org.takingroot.assignment.components.SurveyFormDialog
import org.takingroot.assignment.models.Survey
import org.takingroot.assignment.viewmodels.SurveyViewModel

@Composable
fun ResponseList(viewModel: SurveyViewModel) {

    val surveys: List<Survey>? by viewModel.surveys.observeAsState()

    Column {
        var openDialog by remember {
            mutableStateOf(false)
        }

        Text("Responses", style = MaterialTheme.typography.titleMedium)

        Column {
            surveys?.forEach {
                Text(text = "Survey ${it.payload["first_name"]} ${it.payload["last_name"]}")
            }
        }
        Row {
            Button(onClick = viewModel::refresh) {
                Text(text = "Refresh list")
            }
            Button(onClick = { openDialog = true }) {
                Text(text = "Create samples")
            }
        }
        Button(onClick = { viewModel.send(*surveys.orEmpty().toTypedArray()) }) {
            Text(text = "Send")
        }

        if (openDialog) {
            SurveyFormDialog(viewModel){
                openDialog = false
            }
        }
    }
}

@Preview
@Composable
fun ResponseListPreview() {
    LocalContext.current.applicationContext?.let {
        val viewModel = SurveyViewModel.Factory.create(SurveyViewModel::class.java)
        ResponseList(viewModel = viewModel)
    }
}