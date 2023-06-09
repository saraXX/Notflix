package ui.screens.main

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.vickikbt.shared.presentation.presenters.SharedMainPresenter
import koin
import ui.components.NavigationRailBar
import ui.screens.favourites.FavouritesTab
import ui.screens.home.HomeTab
import ui.screens.settings.SettingsTab
import ui.theme.NotflixDesktopTheme

@Composable
fun MainScreen(applicationScope: ApplicationScope, viewModel: SharedMainPresenter = koin.get()) {
    val appIcon = painterResource("n_logo.png")

    val theme = viewModel.appTheme.collectAsState().value

    Window(
        onCloseRequest = { applicationScope.exitApplication() },
        title = "Notflix",
        icon = appIcon,
        state = rememberWindowState(
            position = WindowPosition.Aligned(Alignment.Center),
            width = Dp.Unspecified,
            height = Dp.Unspecified,
        )
    ) {
        val isDarkTheme = theme != 0

        val topLevelDestinations = listOf(HomeTab, FavouritesTab, SettingsTab)

        NotflixDesktopTheme(darkTheme = isDarkTheme) {
            Surface(color = MaterialTheme.colors.surface) {
                TabNavigator(HomeTab) {
                    Row {
                        NavigationRailBar(
                            tabNavigator = it,
                            navRailItems = topLevelDestinations
                        )

                        CurrentScreen()
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    BottomNavigationItem(
        selected = tabNavigator.current.key == tab.key,
        onClick = { tabNavigator.current = tab },
        icon = { Icon(painter = tab.options.icon!!, contentDescription = tab.options.title) }
    )
}
