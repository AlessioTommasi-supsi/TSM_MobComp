package com.example.smartlock.controller


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

import com.example.smartlock.LogsDeviceView
import com.example.smartlock.LogsDetailView
import com.example.smartlock.view.BT.BTOpenCloseService
import com.example.smartlock.view.BT.CharacteristicListScreen
import com.example.smartlock.view.BT.ParsingMethodListScreen
import com.example.smartlock.view.BT.ServiceListScreen
import com.example.smartlock.view.HomeScreen

@Composable
fun NavigationGraph(navController: NavHostController, innerPadding: PaddingValues) {

    NavHost(navController, startDestination = "scan") {
        composable("scan") { HomeScreen(navController) }
         composable("logs") { LogsDeviceView(navController) }

        composable("logsDetail/{deviceName}",
            arguments = listOf(navArgument("deviceName") { type = NavType.StringType }))
        { backStackEntry ->
            val deviceName = backStackEntry.arguments!!.getString("deviceName")!!
            LogsDetailView(navController, deviceName)
        }
        composable("BTserviceDetail/{deviceAddress}",
            arguments = listOf(navArgument("deviceAddress") { type = NavType.StringType }))
        { backStackEntry ->
            val deviceName = backStackEntry.arguments!!.getString("deviceAddress")!!
            //serviceDetail(deviceName)
            ServiceListScreen(navController, deviceName)
        }
        composable("BTOpenCloseService/{deviceAddress}",
            arguments = listOf(
                navArgument("deviceAddress") { type = NavType.StringType },

            ))
        { backStackEntry ->
            val deviceAddress = backStackEntry.arguments!!.getString("deviceAddress")!!
            BTOpenCloseService(navController, deviceAddress)
        }

        composable("characteristicList/{deviceAddress}/{serviceUuid}",
            arguments = listOf(
                navArgument("deviceAddress") { type = NavType.StringType },
                navArgument("serviceUuid") { type = NavType.StringType }
            ))
        { backStackEntry ->
            val serviceUuid = backStackEntry.arguments!!.getString("serviceUuid")!!
            val deviceAddress = backStackEntry.arguments!!.getString("deviceAddress")!!
            CharacteristicListScreen(navController, deviceAddress = deviceAddress , serviceUuid = serviceUuid)
        }
        composable("parsingMethodList/{deviceAddress}/{serviceUuid}/{characteristicUuid}",
            arguments = listOf(
                navArgument("deviceAddress") { type = NavType.StringType },
                navArgument("serviceUuid") { type = NavType.StringType },
                navArgument("characteristicUuid") { type = NavType.StringType }
            ))
        { backStackEntry ->
            val deviceAddress = backStackEntry.arguments!!.getString("deviceAddress")!!
            val serviceUuid = backStackEntry.arguments!!.getString("serviceUuid")!!
            val characteristicUuid = backStackEntry.arguments!!.getString("characteristicUuid")!!
            ParsingMethodListScreen(navController, deviceAddress = deviceAddress, serviceUuid = serviceUuid, characteristicUuid = characteristicUuid)
        }

        composable("BTread/{deviceAddress}/{serviceUuid}/{characteristicUuid}",
            arguments = listOf(
                navArgument("deviceAddress") { type = NavType.StringType },
                navArgument("serviceUuid") { type = NavType.StringType },
                navArgument("characteristicUuid") { type = NavType.StringType }
            ))
        { backStackEntry ->
            val deviceAddress = backStackEntry.arguments!!.getString("deviceAddress")!!
            val serviceUuid = backStackEntry.arguments!!.getString("serviceUuid")!!
            val characteristicUuid = backStackEntry.arguments!!.getString("characteristicUuid")!!
            ParsingMethodListScreen(navController, deviceAddress = deviceAddress, serviceUuid = serviceUuid, characteristicUuid = characteristicUuid)
        }
    }
}




@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Scan") },
            label = { Text("Scan") },
            selected = navController.currentBackStackEntry?.destination?.route == "scan",
            onClick = { navController.navigate("scan") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Logs") },
            label = { Text("Logs") },
            selected = navController.currentBackStackEntry?.destination?.route == "logs",
            onClick = { navController.navigate("logs") }
        )
    }
}