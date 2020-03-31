package com.marius.personalimdb.database

import androidx.room.*
import com.marius.personalimdb.data.model.HistoryItem

@Dao
interface HistoryDAO {

    @Query("SELECT * FROM historyitem order by modified_at DESC")
    fun getAll(): List<HistoryItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(historyItem: HistoryItem)

    fun insertWithTimestamp(historyItem: HistoryItem) {
        insert(historyItem.apply{
            lastDate = System.currentTimeMillis()
        })
    }

    fun updateWithTimestamp(historyItem: HistoryItem) {
        insert(historyItem.apply{
            lastDate = System.currentTimeMillis()
        })
    }

    @Update
    fun update(historyItem: HistoryItem)

    @Delete
    fun delete(historyItem: HistoryItem)

    @Query("DELETE FROM historyitem")
    fun nukeTable()
}