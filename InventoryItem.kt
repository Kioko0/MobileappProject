package com.strathmore.invapp2.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "inventory_items")
data class InventoryItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,


    @ColumnInfo(name = "item_name")
    var name: String,

    @ColumnInfo(name = "quantity_in_stock")
    var quantity: Int,

    @ColumnInfo(name = "unit_price")
    var price: Double
)
