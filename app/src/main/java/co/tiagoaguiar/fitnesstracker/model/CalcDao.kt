package co.tiagoaguiar.fitnesstracker.model

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface CalcDao {
    @Insert
    fun insert(calc: Calc)
}