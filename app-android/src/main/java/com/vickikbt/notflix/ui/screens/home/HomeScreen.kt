package com.vickikbt.notflix.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.placeholder.material.placeholder
import com.vickikbt.notflix.R
import com.vickikbt.notflix.ui.components.ItemNowPlayingMovies
import com.vickikbt.notflix.ui.components.ItemPopularMovies
import com.vickikbt.notflix.ui.components.ItemTrendingMovies
import com.vickikbt.notflix.ui.components.SectionSeparator
import com.vickikbt.notflix.ui.theme.DarkPrimaryColor
import com.vickikbt.notflix.ui.theme.Gray
import org.koin.androidx.compose.get

@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = get()
) {
    val scrollState = rememberScrollState()
    val pagerState = rememberPagerState()

    LaunchedEffect(key1 = viewModel) {
        viewModel.fetchNowPlayingMovies()
        viewModel.fetchTrendingMovies()
        viewModel.fetchPopularMovies()
        viewModel.fetchUpcomingMovies()
    }

    val homeUiState = viewModel.homeUiState.collectAsState().value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.surface)
    ) {
        if (homeUiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (!homeUiState.error.isNullOrEmpty()) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Error:\n${homeUiState.error}",
                textAlign = TextAlign.Center
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //region Now Playing Movies
                homeUiState.nowPlayingMovies?.let {
                    HorizontalPager(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(360.dp),
                        count = if (it.size >= 5) 5 else it.size,
                        state = pagerState,
                    ) { page ->
                        ItemNowPlayingMovies(
                            modifier = Modifier.fillMaxSize(),
                            // isLoading = homeUiState.isLoading,
                            movie = it[page]
                        ) {
                            val movie = it[page]
                            navController.navigate("details/${movie.id!!}/${movie.cacheId!!}")
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    HorizontalPagerIndicator(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .fillMaxWidth(0.15f)
                            .placeholder(visible = homeUiState.isLoading, color = Color.Gray),
                        pagerState = pagerState,
                        indicatorHeight = 6.dp,
                        indicatorWidth = 6.dp,
                        spacing = 6.dp,
                        activeColor = DarkPrimaryColor,
                        inactiveColor = Gray
                    )
                }
                //endregion

                //region Trending Movies
                SectionSeparator(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, top = 12.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    sectionTitle = stringResource(id = R.string.trending_movies),
                    // isLoading = homeUiState.isLoading,
                    onItemClick = {
                        // ToDo: OnSectionedClicked-navigate to view all
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(items = homeUiState.trendingMovies ?: emptyList()) { item ->
                        ItemTrendingMovies(
                            movie = item,
                            // isLoading = homeUiState.isLoading,
                            onItemClick = { movie ->
                                navController.navigate("details/${movie.id!!}/${movie.cacheId}")
                            }
                        )
                    }
                }
                //endregion

                //region Popular Movies
                homeUiState.popularMovies?.let {
                    SectionSeparator(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, top = 12.dp)
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        sectionTitle = stringResource(id = R.string.popular_movies),
                        // isLoading = homeUiState.isLoading,
                        onItemClick = {
                            // ToDo: OnSectionedClicked-navigate to view all
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier
                            .wrapContentHeight()
                            .placeholder(visible = false, color = Color.Gray)
                    ) {
                        items(items = it) { item ->
                            ItemPopularMovies(
                                modifier = Modifier
                                    .width(300.dp)
                                    .height(245.dp),
                                movie = item,
                                isLoading = homeUiState.isLoading,
                                onClickItem = { movie ->
                                    navController.navigate("details/${movie.id!!}/${movie.cacheId}")
                                }
                            )
                        }
                    }
                }
                //endregion

                //region Upcoming Movies
                homeUiState.upcomingMovies?.let {
                    Column(modifier = Modifier.padding(bottom = 90.dp)) {
                        SectionSeparator(
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp, top = 12.dp)
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            sectionTitle = stringResource(id = R.string.upcoming_movies),
                            // isLoading = homeUiState.isLoading,
                            onItemClick = {
                                // ToDo: OnSectionedClicked-navigate to view all
                            }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(
                            modifier = Modifier.wrapContentHeight(),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            items(items = it) { item ->
                                ItemTrendingMovies(
                                    movie = item,
                                    // isLoading = homeUiState.isLoading,
                                    onItemClick = { movie ->
                                        navController.navigate("details/${movie.id!!}/${movie.cacheId}")
                                    }
                                )
                            }
                        }
                    }
                }
                //endregion
            }
        }
    }
}
