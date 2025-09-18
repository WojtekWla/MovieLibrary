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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Save
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.project_01.model.Category
import com.example.project_01.model.Status
import com.example.project_01.viewModel.AddFilmViewModel
import com.example.project_01.viewModel.EditFilmViewModel
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditView(editFilmViewModel: EditFilmViewModel, onNavigationUp: () -> Unit) {
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
                            text = stringResource(R.string.edit_film),
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
        var displaySuccessDialog by remember {mutableStateOf(false)}
        var displayFailDialog by remember {mutableStateOf(false)}
        Column(
            modifier = Modifier
                .padding(innerPaddings)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if(displaySuccessDialog) {
                SuccessDialog{
                    displaySuccessDialog = false
                    onNavigationUp()
                }
            }

            if(displayFailDialog) {
                FailDialog (
                    editFilmViewModel
                ) {
                    displayFailDialog = false
                }
            }


            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = editFilmViewModel.state!!.title,
                onValueChange = {editFilmViewModel.updateTitle(it)},
                label = { Text(stringResource(R.string.title_label)) }
            )

            Spacer(modifier = Modifier.height(10.dp))


            ChooseImageFromGallery(editFilmViewModel)

            Spacer(modifier = Modifier.height(10.dp))

            ChooseCategory(editFilmViewModel)

            Spacer(modifier = Modifier.height(10.dp))

            ChooseStatus(editFilmViewModel)

            Spacer(modifier = Modifier.height(10.dp))

            ChooseDate(editFilmViewModel)

            if(editFilmViewModel.state!!.status == Status.Seen) {
                Spacer(modifier = Modifier.height(10.dp))
                ChooseRate(editFilmViewModel)
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = editFilmViewModel.state!!.comment,
                    onValueChange = {editFilmViewModel.updateComment(it)},
                    label = { Text(stringResource(R.string.comment_label)) }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                save (
                    onSave = editFilmViewModel::saveFilm,
                    onFail = { displayFailDialog = true},
                    onSuccess = { displaySuccessDialog = true }
                )
            }) {
                Icon(Icons.Default.Save , contentDescription = stringResource(R.string.save_film_to_list))
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseStatus(editFilmViewModel: EditFilmViewModel) {
    var expandedStatus by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expandedStatus,
        onExpandedChange = { expandedStatus = !expandedStatus }
    ) {
        TextField(
            value = editFilmViewModel.state!!.status.toString(),
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
                    editFilmViewModel.updateStatus(Status.Seen)
                    expandedStatus = false
                }
            )
            DropdownMenuItem(
                text = { Text(Status.Unseen.toString()) },
                onClick = {
                    editFilmViewModel.updateStatus(Status.Unseen)
                    expandedStatus = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseDate(editFilmViewModel: EditFilmViewModel) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = editFilmViewModel.state!!.releaseDate
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    )
    editFilmViewModel.updateDate(datePickerState.selectedDateMillis?.let {
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
fun ChooseCategory(editFilmViewModel: EditFilmViewModel) {
    var expandedCategory by remember { mutableStateOf(false ) }

    ExposedDropdownMenuBox(
        expanded = expandedCategory,
        onExpandedChange = { expandedCategory = !expandedCategory }
    ) {
        TextField(
            value = editFilmViewModel.state!!.category.toString(),
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
                    editFilmViewModel.updateCategory(Category.Film)
                    expandedCategory = false
                }
            )
            DropdownMenuItem(
                text = { Text(Category.Series.toString()) },
                onClick = {
                    editFilmViewModel.updateCategory(Category.Series)
                    expandedCategory = false
                }
            )
            DropdownMenuItem(
                text = { Text(Category.Document.toString()) },
                onClick = {
                    editFilmViewModel.updateCategory(Category.Document)
                    expandedCategory = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseImageFromGallery(editFilmViewModel: EditFilmViewModel) {
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
            if (it != null) {
                editFilmViewModel.updateLogo(it)
            }
        }

    Spacer(modifier = Modifier.height(10.dp))

    if(editFilmViewModel.state!!.logoInt != null) {
        Image(
            modifier = Modifier
                .size(150.dp)
                .clip(RectangleShape)
                .padding(10.dp),
            painter = painterResource(editFilmViewModel.state!!.logoInt!!),
            contentDescription = null
        )
    }else {
        AsyncImage(
            model = editFilmViewModel.state!!.logoString,
            modifier = Modifier
                .size(150.dp)
                .clip(RectangleShape)
                .padding(10.dp),
            contentDescription = null
        )
    }

    Button(
        onClick = { galleryLauncher.launch("image/*") },
        modifier = Modifier
            .wrapContentSize()
            .padding(10.dp)
    ) {
        Text(text = stringResource(R.string.pick_icon_description))
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseRate(editFilmViewModel: EditFilmViewModel) {
    var expandedRate by remember{mutableStateOf(false)}

    ExposedDropdownMenuBox(
        expanded = expandedRate,
        onExpandedChange = { expandedRate = !expandedRate }
    ) {
        TextField(
            value = if (editFilmViewModel.state!!.rate == -1) stringResource(R.string.none) else editFilmViewModel.state!!.rate.toString(),
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
                    editFilmViewModel.updateRate(1)
                    expandedRate = false
                }
            )
            DropdownMenuItem(
                text = { Text("2") },
                onClick = {
                    editFilmViewModel.updateRate(2)
                    expandedRate = false
                }
            )
            DropdownMenuItem(
                text = { Text("3") },
                onClick = {
                    editFilmViewModel.updateRate(3)
                    expandedRate = false
                }
            )
            DropdownMenuItem(
                text = { Text("4") },
                onClick = {
                    editFilmViewModel.updateRate(4)
                    expandedRate = false
                }
            )
            DropdownMenuItem(
                text = { Text("5") },
                onClick = {
                    editFilmViewModel.updateRate(5)
                    expandedRate = false
                }
            )
        }
    }
}

@Composable
fun FailDialog(editFilmViewModel: EditFilmViewModel, onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = stringResource(R.string.film_not_saved_message, editFilmViewModel.getErrorMessage()),
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
            )
        }
    }
}