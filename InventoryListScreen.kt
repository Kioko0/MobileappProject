package com.strathmore.invapp2.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.strathmore.invapp2.InventoryViewModel
import com.strathmore.invapp2.data.InventoryItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryListScreen(
    navController: NavController,
    viewModel: InventoryViewModel = viewModel()
) {
    val inventoryItems by viewModel.inventoryItems.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inventory Management") },
                // ADDED: Actions slot for the search icon
                actions = {
                    IconButton(onClick = { navController.navigate("searchItem") }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search Items")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("addItem") }) {
                Icon(Icons.Filled.Add, "Add new item")
            }
        },

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (inventoryItems.isEmpty()) {
                Text("Inventory is empty. Add some items!", style = MaterialTheme.typography.bodyLarge)
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(inventoryItems) { item ->
                        InventoryItemCard(
                            item = item,
                            onEditClick = {
                                navController.navigate("updateItem/${item.id}")
                            },
                            onDeleteClick = {
                                viewModel.deleteItem(item.id)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryItemCard(
    item: InventoryItem,
    onEditClick: (InventoryItem) -> Unit,
    onDeleteClick: (InventoryItem) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEditClick(item) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = item.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "ID: ${item.id}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Quantity: ${item.quantity}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Price: KSh %.2f".format(item.price), style = MaterialTheme.typography.bodyMedium)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onEditClick(item) }) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit Item")
                }
                IconButton(onClick = { onDeleteClick(item) }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete Item")
                }
            }
        }
    }
}
