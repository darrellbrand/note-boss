package com.djf.noteboss

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.djf.noteboss.domain.util.Screen
import com.djf.noteboss.presentation.add_edit_note.components.AddEditNoteScreen
import com.djf.noteboss.presentation.notes.components.NotesScreen
import com.djf.noteboss.ui.theme.NoteBossTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NoteBossTheme {
                val navController = rememberNavController()
                Surface(color = MaterialTheme.colorScheme.background) {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.NotesScreen.route
                    ) {
                        composable(route = Screen.NotesScreen.route) {
                            NotesScreen(navController = navController)
                        }
                        composable(
                            route = Screen.AddEditNotesScreen.route + "?noteId={noteId}&noteColor={noteColor}",
                            arguments = listOf(navArgument(name = "noteId") {
                                type = NavType.IntType
                                defaultValue = -1
                            },
                                navArgument(name = "noteColor") {
                                    type = NavType.IntType
                                    defaultValue = -1
                                })
                        ) {
                            val color = it.arguments?.getInt("noteColor")
                            AddEditNoteScreen(
                                navController = navController,
                                noteColor = color ?: -1
                            )
                        }

                    }
                }
            }
        }
    }
}


