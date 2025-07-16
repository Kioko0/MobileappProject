package com.strathmore.invapp2.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.strathmore.invapp2.InventoryViewModel
import com.strathmore.invapp2.data.InventoryItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchItemScreen(
    navController: NavController,
    viewModel: InventoryViewModel = viewModel()
) {
    var searchTerm by remember { mutableStateOf("") }
    val searchResults = remember(searchTerm) {
        if (searchTerm.isBlank()) {
            emptyList()
        } else {
            viewModel.searchItemsByName(searchTerm)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Inventory") },
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
            OutlinedTextField(
                value = searchTerm,
                onValueChange = { searchTerm = it },
                label = { Text("Search by Item Name") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search Icon") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (searchTerm.isBlank()) {
                Text("Enter a name to search for items.", style = MaterialTheme.typography.bodyLarge)
            } else if (searchResults.isEmpty()) {
                Text("No items found matching '$searchTerm'.", style = MaterialTheme.typography.bodyLarge)
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(searchResults) { item ->
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
