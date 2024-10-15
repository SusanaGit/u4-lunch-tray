/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.lunchtray

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lunchtray.ui.OrderViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lunchtray.datasource.DataSource
import com.example.lunchtray.model.OrderUiState
import com.example.lunchtray.ui.AccompanimentMenuPreview
import com.example.lunchtray.ui.AccompanimentMenuScreen
import com.example.lunchtray.ui.CheckoutScreen
import com.example.lunchtray.ui.EntreeMenuScreen
import com.example.lunchtray.ui.SideDishMenuScreen
import com.example.lunchtray.ui.StartOrderScreen

enum class LunchTrayScreen(@StringRes val title: Int) {
    Start(title = R.string.start_order),
    Entree(title = R.string.choose_entree),
    DishMenu(title = R.string.choose_side_dish),
    Accompaniment(title = R.string.choose_accompaniment),
    CheckOut(title = R.string.order_checkout)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LunchTrayAppBar(
    currentScreen: LunchTrayScreen
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title))}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LunchTrayApp(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = LunchTrayScreen.valueOf(
        backStackEntry?.destination?.route?: LunchTrayScreen.Start.name
    )

    // Create ViewModel
    val viewModel: OrderViewModel = viewModel()

    Scaffold(
        topBar = {
            LunchTrayAppBar(currentScreen = currentScreen)
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = LunchTrayScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = LunchTrayScreen.Start.name) {
                StartOrderScreen(onStartOrderButtonClicked = {
                    navController.navigate(LunchTrayScreen.Entree.name)
                })
            }

            composable(route = LunchTrayScreen.Entree.name) {
                EntreeMenuScreen(
                    options = DataSource.entreeMenuItems,
                    onCancelButtonClicked = { /*TODO*/ },
                    onNextButtonClicked = {
                        navController.navigate(LunchTrayScreen.DishMenu.name)
                    },
                    onSelectionChanged = {}
                )
            }

            composable(route = LunchTrayScreen.DishMenu.name) {
                SideDishMenuScreen(
                    options = DataSource.sideDishMenuItems,
                    onCancelButtonClicked = { /*TODO*/ },
                    onNextButtonClicked = { 
                        navController.navigate(LunchTrayScreen.CheckOut.name)
                    },
                    onSelectionChanged = {}
                )
            }

            composable(route = LunchTrayScreen.Accompaniment.name) {
                AccompanimentMenuScreen(
                    options = DataSource.accompanimentMenuItems,
                    onCancelButtonClicked = { /*TODO*/ },
                    onNextButtonClicked = { 
                        navController.navigate(LunchTrayScreen.Accompaniment.name)
                    },
                    onSelectionChanged = {}
                )
            }

            composable(route = LunchTrayScreen.CheckOut.name) {
                CheckoutScreen(
                    orderUiState = OrderUiState(),
                    onNextButtonClicked = {
                        navController.navigate(LunchTrayScreen.CheckOut.name)
                    },
                    onCancelButtonClicked = { /*TODO*/ })
            }
        }
    }
}
