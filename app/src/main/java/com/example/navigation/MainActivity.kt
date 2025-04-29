package com.example.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.navigation.ui.theme.NavigationTheme
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TaskViewModel: ViewModel() {
    private val _selectedTask = MutableStateFlow<Task?>(null)
    val selectedTask: StateFlow<Task?> = _selectedTask

    fun selectTask(task: Task) {
        _selectedTask.value = task
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavigationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val taskViewModel: TaskViewModel = viewModel()
                    AppNavigation(taskViewModel)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(taskViewModel: TaskViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(navController, taskViewModel)
        }
        composable("taskScreen") {
            TaskScreen(navController, taskViewModel)
        }
    }
}

@Composable
fun HomeScreen(navController: NavController, taskViewModel: TaskViewModel) {
    val tasks = Task("buy milk", false)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Home Screen")
        Button(onClick = {
            taskViewModel.selectTask(tasks)
            navController.navigate("taskScreen")
        }) {
            Text(text = "goto ${tasks.title}")
        }
    }
}

@Composable
fun TaskScreen(navController: NavController, taskViewModel: TaskViewModel) {
    val task = taskViewModel.selectedTask.collectAsState().value
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "task: ${task?.title}")
        Text(text = "done: ${task?.isDone}")
        Button(onClick = { navController.navigate("home") }) {
            Text(text = "goto home")
        }
    }
}