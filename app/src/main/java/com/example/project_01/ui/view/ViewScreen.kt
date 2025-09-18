package com.example.project_01.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.project_01.R
import com.example.project_01.model.Category
import com.example.project_01.model.Film
import com.example.project_01.model.Status
import com.example.project_01.viewModel.FilmViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewScreen(
    filmViewModel: FilmViewModel,
    onNavigationEdit: () -> Unit,
    onNavigationUp: () -> Unit,

    ) {
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
                            text = stringResource(R.string.details),
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
                actions = {
                    if(filmViewModel.state!!.status == Status.Unseen) {
                        Icon(
                            modifier = Modifier
                                .clickable(onClick = onNavigationEdit)
                                .padding(12.dp),
                            imageVector = Icons.Filled.Edit,
                            contentDescription = stringResource(R.string.go_to_edit_view)
                        )
                    }
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
        Column(
            modifier = Modifier
                .padding(innerPaddings)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = filmViewModel.state!!.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            if(filmViewModel.state!!.logoInt != null) {
                Image(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RectangleShape)
                        .padding(10.dp),
                    painter = painterResource(filmViewModel.state!!.logoInt!!),
                    contentDescription = null,
                )
            }else {
                AsyncImage(
                    model = filmViewModel.state!!.logoString,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RectangleShape)
                        .padding(10.dp),
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row  (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
            ){
                Text(
                    text = stringResource(R.string.releaseDate),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = filmViewModel.state!!.releaseDate.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
            ) {
                Text(
                    text = stringResource(R.string.category),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = filmViewModel.state!!.category.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 20.sp
                )
            }
            
            Spacer(modifier = Modifier.height(10.dp))

            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
            ) {
                Text(
                    text = stringResource(R.string.status),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = filmViewModel.state!!.status.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 20.sp
                )
            }

            if(filmViewModel.state!!.status == Status.Seen) {
                Spacer(modifier = Modifier.height(10.dp))
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Text(
                        text = stringResource(R.string.rate),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                    Text(
                        modifier = Modifier.padding(start = 10.dp),
                        text = filmViewModel.state!!.rate.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 20.sp
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Text(
                        text = stringResource(R.string.comment),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                    Text(
                        modifier = Modifier.padding(start = 10.dp),
                        text = filmViewModel.state!!.comment.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}