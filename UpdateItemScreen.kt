package com.strathmore.invapp2.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.strathmore.invapp2.InventoryViewModel
import com.strathmore.invapp2.data.InventoryItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateItemScreen(
    navController: NavController,
    itemId: Int,
    viewModel: InventoryViewModel = viewModel()
) {
    val itemToUpdate = viewModel.findItemById(itemId)

    var itemName by remember(itemToUpdate) { mutableStateOf(itemToUpdate?.name ?: "") }
    var itemQuantity by remember(itemToUpdate) { mutableStateOf(itemToUpdate?.quantity?.toString() ?: "") }
    var itemPrice by remember(itemToUpdate) { mutableStateOf(itemToUpdate?.price?.toString() ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Update Item (ID: $itemId)") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (itemToUpdate == null) {
                Text("Item not found.", style = MaterialTheme.typography.headlineSmall)
            } else {
                OutlinedTextField(
                    value = itemName,
                    onValueChange = { itemName = it },
                    label = { Text("Item Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = itemQuantity,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() } || newValue.isEmpty()) {
                            itemQuantity = newValue
                        }
                    },
                    label = { Text("Quantity") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = itemPrice,
                    onValueChange = { newValue ->
                        val filteredValue = newValue.filter { it.isDigit() || it == '.' }
                        if (filteredValue.count { it == '.' } <= 1) {
                            itemPrice = filteredValue
                        }
                    },
                    label = { Text("Price (KSh)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        val quantityInt = itemQuantity.toIntOrNull()
                        val priceDouble = itemPrice.toDoubleOrNull()

                        if (itemName.isNotBlank() && quantityInt != null && quantityInt >= 0 && priceDouble != null && priceDouble >= 0.0) {
                            val updatedItem = InventoryItem(
                                id = itemId,
                                name = itemName,
                                quantity = quantityInt,
                                price = priceDouble
                            )
                            viewModel.updateItem(updatedItem)
                            navController.popBackStack()
                        } else {
                            println("Invalid input. Please fill all fields correctly.")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Update Item")
                }
            }
        }
    }
}
