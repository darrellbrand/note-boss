package com.djf.noteboss.presentation.add_edit_note.components

import android.annotation.SuppressLint
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.djf.noteboss.domain.model.Note
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    noteColor: Int,
    viewModel: AddEditNoteViewModel = hiltViewModel(),

    ) {
    val titleState by viewModel.noteTitle
    val contentState by viewModel.noteContent
    val scaffoldState = rememberScaffoldState()
    val noteBackgroundAnimateable = remember {
        Animatable(
            Color(if (noteColor != -1) noteColor else viewModel.noteColor.value)
        )
    }
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditNoteViewModel.UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short
                    )
                }

                AddEditNoteViewModel.UiEvent.SaveNote -> {
                    navController.navigateUp()
                }
            }
        }
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(AddEditNoteEvent.SaveNote) },
                backgroundColor = MaterialTheme.colorScheme.primary, modifier = Modifier
            ) {
                Icon(imageVector = Icons.Default.Star, contentDescription = " save note")
            }

        }, scaffoldState = scaffoldState, modifier = Modifier
            .imePadding()
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(noteBackgroundAnimateable.value)
                .systemBarsPadding()
                .imePadding()
                .padding(15.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Note.noteColors.forEach { color ->
                    val colorInt = color.toArgb()
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .shadow(50.dp, CircleShape)
                            .clip(CircleShape)
                            .background(color)

                            .border(
                                width = 3.dp,
                                color = if (viewModel.noteColor.value == colorInt) {
                                    Color.Black
                                } else {
                                    Color.Transparent
                                }, shape = CircleShape
                            )

                            .clickable {
                                scope.launch {
                                    noteBackgroundAnimateable.animateTo(
                                        targetValue = Color(colorInt),
                                        animationSpec = tween(durationMillis = 500)
                                    )
                                }
                                viewModel.onEvent(AddEditNoteEvent.ChangeColor(colorInt))
                            }
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1 / 8f)
            ) {
                Spacer(modifier = Modifier.width(15.dp))
                Switch(
                    checked = contentState.isLinkEnabled,
                    onCheckedChange = { viewModel.onEvent(AddEditNoteEvent.ToggleSwitch(it)) },
                    modifier = Modifier, colors = androidx.compose.material3.SwitchDefaults.colors(
                        checkedThumbColor = Color(viewModel.noteColor.value),//MaterialTheme.colorScheme.primary,
                        checkedTrackColor = MaterialTheme.colorScheme.onPrimary,
                        uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                        uncheckedTrackColor = MaterialTheme.colorScheme.onSecondary,
                    )
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Create Cloud Link", style = MaterialTheme.typography.titleMedium)
            }
            Spacer(modifier = Modifier.height(15.dp))
            TransparentHintTextField(
                text = titleState.text,
                hint = "Enter title",
                modifier = Modifier,
                onValueChange = { viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it)) },
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(it))
                },
                isHintVisible = titleState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineMedium
            )
            Divider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
                thickness = 3.dp
            )
            Spacer(modifier = Modifier.height(15.dp))
            TransparentHintTextField(
                text = contentState.text,
                hint = "Enter content",//contentState.hint,
                modifier = Modifier.weight(1 / 2f),
                onValueChange = { viewModel.onEvent(AddEditNoteEvent.EnteredContent(it)) },
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(it))
                },
                isHintVisible = contentState.isHintVisible,
                singleLine = false,
                textStyle = MaterialTheme.typography.titleLarge
            )
            if (contentState.isLinkEnabled && contentState.link.isNotBlank()) {
                Divider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
                    thickness = 3.dp
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Cloud Link",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.height(15.dp))
                SelectionContainer {
                    val annotatedString = buildAnnotatedString {
                        val link = viewModel.noteContent.value.link
                        append(link)
                        addStyle(
                            style = SpanStyle(
                                color = Color.Blue,
                                textDecoration = TextDecoration.Underline
                            ), start = 0, end = link.length
                        )
                        addStringAnnotation(
                            tag = "URL",
                            annotation = link,
                            start = 0,
                            end = link.length
                        )
                    }
                    val mUriHandler = LocalUriHandler.current
                    ClickableText(text = annotatedString, onClick = {
                        annotatedString
                            .getStringAnnotations("URL", it, it)
                            .firstOrNull()?.let { stringAnnotation ->
                                mUriHandler.openUri(stringAnnotation.item)
                            }
                    })

                }

            }
        }
    }
}