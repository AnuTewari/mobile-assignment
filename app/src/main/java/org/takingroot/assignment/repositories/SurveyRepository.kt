package org.takingroot.assignment.repositories

import androidx.lifecycle.MutableLiveData
import org.takingroot.assignment.models.Survey
import org.takingroot.assignment.models.SurveyDao

interface ISurveyRepository {
    var surveys: MutableLiveData<List<Survey>>
    suspend fun delete(vararg survey: Survey)
    suspend fun save(survey: Survey)
    suspend fun fetchAll()
}

class SurveyRepository(private val surveyDao: SurveyDao) : ISurveyRepository {
    override var surveys: MutableLiveData<List<Survey>> = MutableLiveData()

    override suspend fun delete(vararg survey: Survey) = surveyDao.delete(*survey)

    override suspend fun save(survey: Survey) = surveyDao.save(survey)

    override suspend fun fetchAll() = surveys.postValue(surveyDao.getAll())

}