package com.strathmore.invapp2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.strathmore.invapp2.data.AppDatabase
import com.strathmore.invapp2.data.InventoryDao
import com.strathmore.invapp2.screens.AddItemScreen
import com.strathmore.invapp2.screens.InventoryListScreen
import com.strathmore.invapp2.screens.SearchItemScreen
import com.strathmore.invapp2.screens.UpdateItemScreen
import com.strathmore.invapp2.ui.theme.SmallBusinessInventoryTheme

class MainActivity : ComponentActivity() {


    private val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

    private val inventoryDao: InventoryDao by lazy { database.inventoryDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmallBusinessInventoryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    InventoryAppNavigation(inventoryDao = inventoryDao)
                }
            }
        }
    }
}


class InventoryViewModelFactory(private val inventoryDao: InventoryDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(inventoryDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


@Composable
fun InventoryAppNavigation(inventoryDao: InventoryDao) {
    val navController = rememberNavController()

    val inventoryViewModel: InventoryViewModel = viewModel(factory = InventoryViewModelFactory(inventoryDao))

    NavHost(navController = navController, startDestination = "inventoryList") {
        composable("inventoryList") {
            InventoryListScreen(navController = navController, viewModel = inventoryViewModel)
        }
        composable("addItem") {
            AddItemScreen(navController = navController, viewModel = inventoryViewModel)
        }
        composable(
            "updateItem/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId")
            if (itemId != null) {
                UpdateItemScreen(navController = navController, itemId = itemId, viewModel = inventoryViewModel)
            } else {
                navController.popBackStack()
            }
        }
        composable("searchItem") {
            SearchItemScreen(navController = navController, viewModel = inventoryViewModel)
        }
    }
}
