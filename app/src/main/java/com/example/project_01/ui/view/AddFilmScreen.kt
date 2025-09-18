package com.example.project_01.ui.view

import com.example.project_01.R
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.project_01.model.Category
import com.example.project_01.model.Status
import com.example.project_01.viewModel.AddFilmViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddView(addFilmViewModel: AddFilmViewModel, onNavigationUp: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row (verticalAlignment = Alignment.CenterVertically){
                        Image(
                            painter = painterResource(id = R.drawable.movie_book),
                            contentDescription = stringResource(R.string.logo),
                            modifier = Modifier
                                .size(50.dp)
                                .padding(end = 8.dp)
                        )
                        Text(
                            text = stringResource(R.string.add_new_film),
                            color = Color.Black
                        )
                    }
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .clickable(onClick = onNavigationUp)
                            .padding(12.dp),
                        imageVector = Icons.Default.Home,
                        contentDescription = stringResource(R.string.go_back_home)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF1976D2),
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                )
            )
        }
    ) { innerPaddings->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(innerPaddings)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var displaySuccessDialog by remember {mutableStateOf(false)}
            var displayFailDialog by remember {mutableStateOf(false)}



            ChooseImageFromGallery(addFilmViewModel)

            if(displaySuccessDialog) {
                SuccessDialog{
                    displaySuccessDialog = false
                    onNavigationUp()
                }
            }

            if(displayFailDialog) {
                FailDialog(addFilmViewModel) {
                    displayFailDialog = false
                }
            }


            OutlinedTextField(
                value = addFilmViewModel.state.title,
                onValueChange = {addFilmViewModel.updateTitle(it)},
                label = { Text(stringResource(R.string.title_label)) }
            )

            Spacer(modifier = Modifier.height(10.dp))

            ChooseCategory(addFilmViewModel)

            Spacer(modifier = Modifier.height(10.dp))

            ChooseStatus(addFilmViewModel)

            ChooseDate(addFilmViewModel)

            if(addFilmViewModel.state.status == Status.Seen) {
                ChooseRate(addFilmViewModel)
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = addFilmViewModel.state.comment,
                    onValueChange = {addFilmViewModel.updateComment(it)},
                    label = { Text(stringResource(R.string.comment_label)) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            Button(onClick = {
                save(onSave = addFilmViewModel::saveFilm, { displayFailDialog = true }, { displaySuccessDialog = true })
            }) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_film_button))
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseStatus(addFilmViewModel: AddFilmViewModel) {
    var expandedStatus by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expandedStatus,
        onExpandedChange = { expandedStatus = !expandedStatus }
    ) {
        TextField(
            value = addFilmViewModel.state.status.toString(),
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.status_label)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStatus)
            },
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expandedStatus,
            onDismissRequest = { expandedStatus = false }
        ) {
            DropdownMenuItem(
                text = { Text(Status.Seen.toString()) },
                onClick = {
                    addFilmViewModel.updateStatus(Status.Seen)
                    expandedStatus = false
                }
            )
            DropdownMenuItem(
                text = { Text(Status.Unseen.toString()) },
                onClick = {
                    addFilmViewModel.updateStatus(Status.Unseen)
                    expandedStatus = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseDate(addFilmViewModel: AddFilmViewModel) {
    val datePickerState = rememberDatePickerState()
    addFilmViewModel.updateDate(datePickerState.selectedDateMillis?.let {
        convertMillisToLocalDate(it)
    })
    Box(
        modifier = Modifier
            .scale(0.80f)
            .offset(y = 16.dp)
            .shadow(elevation = 4.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        DatePicker(
            state = datePickerState,
            showModeToggle = false
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseCategory(addFilmViewModel: AddFilmViewModel) {
    var expandedCategory by remember { mutableStateOf(false ) }

    ExposedDropdownMenuBox(
        expanded = expandedCategory,
        onExpandedChange = { expandedCategory = !expandedCategory }
    ) {
        TextField(
            value = addFilmViewModel.state.category.toString(),
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.category_label)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory)
            },
            modifier = Modifier.menuAnchor()
        )
        Spacer(modifier = Modifier.height(10.dp))
        ExposedDropdownMenu(
            expanded = expandedCategory,
            onDismissRequest = { expandedCategory = false }
        ) {
            DropdownMenuItem(
                text = { Text(Category.Film.toString()) },
                onClick = {
                    addFilmViewModel.updateCategory(Category.Film)
                    expandedCategory = false
                }
            )
            DropdownMenuItem(
                text = { Text(Category.Series.toString()) },
                onClick = {
                    addFilmViewModel.updateCategory(Category.Series)
                    expandedCategory = false
                }
            )
            DropdownMenuItem(
                text = { Text(Category.Document.toString()) },
                onClick = {
                    addFilmViewModel.updateCategory(Category.Document)
                    expandedCategory = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseImageFromGallery(addFilmViewModel: AddFilmViewModel) {
    var displayImage by remember {mutableStateOf(false)}
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
            if (it != null) {
                displayImage = true
                addFilmViewModel.updateImage(it)
            }
        }

    Spacer(modifier = Modifier.height(10.dp))

    Button(
        onClick = { galleryLauncher.launch("image/*") },
        modifier = Modifier
            .wrapContentSize()
            .padding(10.dp)
    ) {
        Text(text = stringResource(R.string.pick_icon_description))
    }

    if(displayImage) {
        AsyncImage(
            model = addFilmViewModel.state.image,
            modifier = Modifier
                .size(150.dp)
                .clip(RectangleShape)
                .padding(10.dp),
            contentDescription = stringResource(R.string.image_from_gallery_description)
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseRate(addFilmViewModel: AddFilmViewModel) {
    var expandedRate by remember{mutableStateOf(false)}

    ExposedDropdownMenuBox(
        expanded = expandedRate,
        onExpandedChange = { expandedRate = !expandedRate }
    ) {
        TextField(
            value = if (addFilmViewModel.state.rate == -1) stringResource(R.string.none) else addFilmViewModel.state.rate.toString(),
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.rate_label)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRate)
            },
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expandedRate,
            onDismissRequest = { expandedRate = false }
        ) {
            DropdownMenuItem(
                text = { Text("1") },
                onClick = {
                    addFilmViewModel.updateRate(1)
                    expandedRate = false
                }
            )
            DropdownMenuItem(
                text = { Text("2") },
                onClick = {
                    addFilmViewModel.updateRate(2)
                    expandedRate = false
                }
            )
            DropdownMenuItem(
                text = { Text("3") },
                onClick = {
                    addFilmViewModel.updateRate(3)
                    expandedRate = false
                }
            )
            DropdownMenuItem(
                text = { Text("4") },
                onClick = {
                    addFilmViewModel.updateRate(4)
                    expandedRate = false
                }
            )
            DropdownMenuItem(
                text = { Text("5") },
                onClick = {
                    addFilmViewModel.updateRate(5)
                    expandedRate = false
                }
            )
        }
    }
}



@Composable
fun SuccessDialog(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = stringResource(R.string.saved_film_message),
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun FailDialog(addFilmViewModel: AddFilmViewModel, onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = stringResource(R.string.film_not_saved_message, addFilmViewModel.getErrorMessage()), //possible error
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
            )
        }
    }
}


fun save(onSave: () -> Boolean, onFail: () -> Unit, onSuccess: () -> Unit) {
    var saved = onSave()
    if(saved) {
        onSuccess()
    }else {
        onFail()
    }
}

fun convertMillisToLocalDate(millis: Long): LocalDate {
    val instant = Instant.ofEpochMilli(millis)
    return instant.atZone(ZoneId.systemDefault()).toLocalDate()
}
