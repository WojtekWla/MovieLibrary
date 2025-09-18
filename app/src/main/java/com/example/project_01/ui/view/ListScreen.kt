    package com.example.project_01.ui.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.project_01.R
import com.example.project_01.model.Category
import com.example.project_01.model.Film
import com.example.project_01.model.Status
import com.example.project_01.viewModel.ListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListOfFilms(listViewModel: ListViewModel, onFilmClicked: (Int) -> Unit, onAddClicked: () -> Unit) {
    Scaffold (
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
                            text = stringResource(R.string.ListOfMoviesTitle),
                            color = Color.Black
                        )
                    }
                },
                actions = {
                    Icon(
                        modifier = Modifier
                            .padding(12.dp)
                            .clickable(onClick = onAddClicked),
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(R.string.iconAddNewElementContentDescription)
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
    ){ innerPaddings ->
        val systemPaddings = WindowInsets.systemBars.asPaddingValues()
        var openDialog by remember { mutableStateOf(false) }
        Column (
            modifier = Modifier
                .padding(innerPaddings)
                .padding(horizontal = 8.dp)
                .padding(bottom = systemPaddings.calculateBottomPadding())
                .fillMaxSize()

        ) {
            Spacer(modifier = Modifier.height(10.dp))
            SortingButtons(listViewModel)
            PositionSummary(listViewModel)
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = innerPaddings,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(listViewModel.state.list, key = { it.id }) {
                    FilmElement(
                        it,
                        onClicked = { onFilmClicked(it.id) },
                        onLongClicked = {
                            openDialog = true
                            listViewModel.updateSelectedFilm(it)
                        }
                    )
                }
            }
            if(openDialog) {
                DeletePositionDialog(
                    film = listViewModel.state.selectedFilm!!,
                    onDismissRequest = { listViewModel.updateSelectedFilm(null); openDialog = false },
                    onConfirmation = { listViewModel.removeFilm(); openDialog = false }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FilmElement(film: Film, onClicked: () -> Unit, onLongClicked: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClicked,
                onLongClick = onLongClicked
            ),
        shape = RoundedCornerShape(16.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            if(film.logoInt != null) {
                Image(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RectangleShape)
                        .padding(10.dp),
                    painter = painterResource(film.logoInt!!),
                    contentDescription = null
                )
            }else {
                AsyncImage(
                    model = film.logoString,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RectangleShape)
                        .padding(10.dp),
                    contentDescription = null
                )
            }
            Column (modifier = Modifier
                .padding(10.dp)
            )
            {
                Row() {
                    Text(stringResource(R.string.title), fontWeight = FontWeight.Bold)
                    Text(film.title, modifier = Modifier.padding(start = 5.dp))
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row() {
                    Text(stringResource(R.string.releaseDate), fontWeight = FontWeight.Bold)
                    Text(film.releaseDate.toString(), modifier = Modifier.padding(start = 5.dp))
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row() {
                    Text(stringResource(R.string.category), fontWeight = FontWeight.Bold)
                    Text(film.category.toString(), modifier = Modifier.padding(start = 5.dp))
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row() {
                    Text(stringResource(R.string.status), fontWeight = FontWeight.Bold)
                    Text(film.status.toString(), modifier = Modifier.padding(start = 5.dp))
                }
                if(film.status == Status.Seen) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row() {
                        Text(stringResource(R.string.rate), fontWeight = FontWeight.Bold)
                        Text(film.rate.toString(), modifier = Modifier.padding(start = 5.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortingButtons(
    listViewModel: ListViewModel
) {
    var expandedCategory by remember { mutableStateOf(false ) }
    var expandedStatus by remember { mutableStateOf(false) }
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            ExposedDropdownMenuBox(
                expanded = expandedCategory,
                onExpandedChange = { expandedCategory = !expandedCategory }
            ) {
                TextField(
                    value = listViewModel.state.category?.toString() ?: stringResource(R.string.none),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.category_label)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory)
                    },
                    modifier = Modifier
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedCategory,
                    onDismissRequest = { expandedCategory = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.none)) },
                        onClick = {
                            listViewModel.updateCategory(null)
                            expandedCategory = false
                        }
                    )

                    DropdownMenuItem(
                        text = { Text(Category.Film.toString()) },
                        onClick = {
                            listViewModel.updateCategory(Category.Film)
                            expandedCategory = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(Category.Series.toString()) },
                        onClick = {
                            listViewModel.updateCategory(Category.Series)
                            expandedCategory = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(Category.Document.toString()) },
                        onClick = {
                            listViewModel.updateCategory(Category.Document)
                            expandedCategory = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(10.dp))
        Box(modifier = Modifier.weight(1f)) {
            ExposedDropdownMenuBox(
                expanded = expandedStatus,
                onExpandedChange = { expandedStatus = !expandedStatus }
            ) {
                TextField(
                    value = listViewModel.state.status?.toString() ?: stringResource(R.string.none),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.status_label)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStatus)
                    },
                    modifier = Modifier
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedStatus,
                    onDismissRequest = { expandedStatus = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.none)) },
                        onClick = {
                            listViewModel.updateStatus(null)
                            expandedStatus = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(Status.Seen.toString()) },
                        onClick = {
                            listViewModel.updateStatus(Status.Seen)
                            expandedStatus = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(Status.Unseen.toString()) },
                        onClick = {
                            listViewModel.updateStatus(Status.Unseen)
                            expandedStatus = false
                        }
                    )
                }
            }
        }

        Button(onClick = {
             listViewModel.updateList()
        }) {
            Icon(Icons.Filled.FilterAlt, contentDescription = stringResource(R.string.filter_movies_description))
        }
    }
}

@Composable
fun PositionSummary(listViewModel: ListViewModel) {
    Text(
        text = stringResource(R.string.number_of_positions, listViewModel.getNumberOfPositions()),
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun DeletePositionDialog(
    film: Film,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if(film.logoInt != null) {
                    Image(
                        painter = painterResource(film.logoInt!!),
                        contentDescription = stringResource(R.string.film_image_description, film.title),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(150.dp)
                    )
                }else {
                    AsyncImage(
                        model = film.logoString,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(RectangleShape)
                            .padding(10.dp),
                        contentDescription = null
                    )
                }
                Text(
                    text = stringResource(R.string.delete_movie_question, film.title),
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(R.string.dismiss_button))
                    }
                    TextButton(
                        onClick = { onConfirmation() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(R.string.delete_button))
                    }
                }
            }
        }
    }
}
