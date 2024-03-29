package co.tiagoaguiar.fitnesstracker.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CalcDao {
    @Insert
    fun insert(calc: Calc)
    @Query("SELECT * FROM Calc WHERE type = :type")
    fun findByType(type: String): MutableList<Calc>
    @Delete
    fun delete(calc: Calc)

}