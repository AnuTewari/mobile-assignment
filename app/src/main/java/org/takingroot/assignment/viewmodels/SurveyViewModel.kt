package org.takingroot.assignment.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.google.gson.Gson
import com.google.gson.JsonDeserializer
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.takingroot.assignment.models.AppDatabase
import org.takingroot.assignment.models.Survey
import org.takingroot.assignment.networking.APIResponse
import org.takingroot.assignment.networking.BaseAPIService
import org.takingroot.assignment.networking.RetrofitInstance
import org.takingroot.assignment.networking.UserResponse
import org.takingroot.assignment.repositories.ISurveyRepository
import org.takingroot.assignment.repositories.SurveyRepository
import org.takingroot.assignment.utils.converters.GSONConverter
import org.takingroot.assignment.utils.converters.fromJson


class SurveyViewModel(
    private val repository: ISurveyRepository,
    private val apiService: BaseAPIService,
    private val gsonConverter: GSONConverter
) : ViewModel() {
    val surveys = repository.surveys

    init {
        this.refresh()
    }

    fun send(vararg surveys: Survey) = viewModelScope.launch(Dispatchers.IO) {
        withContext(this.coroutineContext) {
            surveys.forEach {
                val jsonString = gsonConverter.fromMap(it.payload)
                var bodyAsJsonObject = JsonParser.parseString(jsonString).asJsonObject
                apiService.response(it.name, bodyAsJsonObject)
                repository.delete(it)
            }
            repository.fetchAll()
        }
    }

    fun save(survey: Survey) = viewModelScope.launch(Dispatchers.IO) {
        withContext(this.coroutineContext) {
            repository.save(
                survey
            )
        }
        refresh()
    }

    fun refresh() = viewModelScope.launch(Dispatchers.IO) {
        withContext(this.coroutineContext) { repository.fetchAll() }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[APPLICATION_KEY])

                val db = AppDatabase.getDatabase(application)
                val repository = SurveyRepository(db.surveyDao())
                val gsonConverter = GSONConverter()
                return SurveyViewModel(repository, RetrofitInstance.getInstance(), gsonConverter) as T
            }
        }
    }
}