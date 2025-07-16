package com.strathmore.invapp2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.strathmore.invapp2.data.InventoryDao
import com.strathmore.invapp2.data.InventoryItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class InventoryViewModel(private val inventoryDao: InventoryDao) : ViewModel() {


    val inventoryItems: StateFlow<List<InventoryItem>> =
        inventoryDao.getAllItems()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    
    

    fun addItem(name: String, quantity: Int, price: Double) {
        viewModelScope.launch {
            val newItem = InventoryItem(name = name, quantity = quantity, price = price)
            inventoryDao.insert(newItem)
        }
    }

    fun updateItem(updatedItem: InventoryItem) {
        viewModelScope.launch {
            inventoryDao.update(updatedItem)
        }
    }

    fun deleteItem(itemId: Int) {
        viewModelScope.launch {
            val itemToDelete = inventoryItems.value.find { it.id == itemId }
            if (itemToDelete != null) {
                inventoryDao.delete(itemToDelete)
            }
        }
    }

    fun searchItemsByName(searchTerm: String): List<InventoryItem> {

        return inventoryItems.value.filter {
            it.name.contains(searchTerm, ignoreCase = true)
        }
    }

    fun findItemById(id: Int): InventoryItem? {

        return inventoryItems.value.find { it.id == id }
    }
}
