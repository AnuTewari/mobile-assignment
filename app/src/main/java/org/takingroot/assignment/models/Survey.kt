package org.takingroot.assignment.models

import androidx.annotation.NonNull
import androidx.room.*
import com.google.gson.annotations.Expose
import java.util.*

@Entity(tableName = "surveys")
data class Survey(
    @Expose
    @PrimaryKey
    @NonNull
    var id: UUID = UUID.randomUUID(),

    @NonNull
    var name: String = "",

    @NonNull
    @Expose
    var payload: Map<String, Any> = mapOf()
)

@Dao
interface SurveyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(survey: Survey)

    @Query("select * from surveys")
    suspend fun getAll(): List<Survey>

    @Delete
    suspend fun delete(vararg surveys: Survey)
}