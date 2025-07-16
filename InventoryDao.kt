package com.strathmore.invapp2.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryDao {


    @Query("SELECT * FROM inventory_items ORDER BY item_name ASC")
    fun getAllItems(): Flow<List<InventoryItem>>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: InventoryItem)


    @Update
    suspend fun update(item: InventoryItem)


    @Delete
    suspend fun delete(item: InventoryItem) // 'suspend' makes it a coroutine function


    @Query("SELECT * FROM inventory_items WHERE id = :itemId")
    fun getItemById(itemId: Int): Flow<InventoryItem?>
}
