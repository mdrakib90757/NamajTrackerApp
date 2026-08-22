package com.example.namajtrackerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.namajtrackerapp.localization.AppStrings
import com.example.namajtrackerapp.model.AppLanguage
import com.example.namajtrackerapp.model.PrayerStatus
import com.example.namajtrackerapp.ui.components.AddEditEventBottomSheet
import com.example.namajtrackerapp.ui.components.AddEditNoteBottomSheet
import com.example.namajtrackerapp.ui.components.AnimatedDeleteConfirmationDialog
import com.example.namajtrackerapp.ui.components.QuickPrayerStatusBottomSheet
import com.example.namajtrackerapp.ui.screens.CalendarScreen
import com.example.namajtrackerapp.ui.screens.EventReportScreen
import com.example.namajtrackerapp.ui.screens.EventsScreen
import com.example.namajtrackerapp.ui.screens.HomeScreen
import com.example.namajtrackerapp.ui.screens.NotesScreen
import com.example.namajtrackerapp.ui.screens.OnboardingScreen
import com.example.namajtrackerapp.ui.screens.ProfileScreen
import com.example.namajtrackerapp.ui.screens.ReflectionReportScreen
import com.example.namajtrackerapp.ui.screens.SalatReportScreen
import com.example.namajtrackerapp.ui.theme.ClayBrownPrimary
import com.example.namajtrackerapp.ui.theme.NamazTrackerTheme
import com.example.namajtrackerapp.ui.theme.SoftButterAccent
import com.example.namajtrackerapp.viewmodel.NamazViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: NamazViewModel = viewModel()
            val userSettings by viewModel.userSettings.collectAsState()

            NamazTrackerTheme(themeMode = userSettings.themeMode) {
                MainApp(viewModel = viewModel)
            }
        }
    }
}

sealed class Screen(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val labelProvider: (AppLanguage) -> String
) {
    data object Home : Screen(
        route = "home",
        selectedIcon = Icons.Rounded.Home,
        unselectedIcon = Icons.Outlined.Home,
        labelProvider = { AppStrings.tabHome(it) }
    )
    data object Calendar : Screen(
        route = "calendar",
        selectedIcon = Icons.Rounded.CalendarMonth,
        unselectedIcon = Icons.Outlined.CalendarMonth,
        labelProvider = { AppStrings.tabCalendar(it) }
    )
    data object Events : Screen(
        route = "events",
        selectedIcon = Icons.Rounded.Event,
        unselectedIcon = Icons.Outlined.Event,
        labelProvider = { AppStrings.tabEvents(it) }
    )
    data object Notes : Screen(
        route = "notes",
        selectedIcon = Icons.Rounded.EditNote,
        unselectedIcon = Icons.Outlined.EditNote,
        labelProvider = { AppStrings.tabNotes(it) }
    )
    data object Profile : Screen(
        route = "profile",
        selectedIcon = Icons.Rounded.Person,
        unselectedIcon = Icons.Outlined.Person,
        labelProvider = { AppStrings.tabProfile(it) }
    )
}

@Composable
fun MainApp(viewModel: NamazViewModel) {
    val navController = rememberNavController()
    val userSettings by viewModel.userSettings.collectAsState()
    val language = userSettings.language

    // Check if onboarding needs to be shown
    var isReplayingOnboarding by remember { mutableStateOf(false) }
    val showOnboarding = !userSettings.hasCompletedOnboarding || isReplayingOnboarding

    if (showOnboarding) {
        OnboardingScreen(
            language = language,
            onFinish = {
                viewModel.setOnboardingCompleted(true)
                isReplayingOnboarding = false
            }
        )
        return
    }

    // Active bottom sheet & dialog states
    val deleteDialogState by viewModel.deleteDialogState.collectAsState()
    val activePrayerPicker by viewModel.activePrayerPicker.collectAsState()
    val isEventEditorOpen by viewModel.isEventEditorOpen.collectAsState()
    val editingEvent by viewModel.editingEvent.collectAsState()
    val isNoteEditorOpen by viewModel.isNoteEditorOpen.collectAsState()
    val editingNote by viewModel.editingNote.collectAsState()
    val prayerRecords by viewModel.prayerRecords.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            NamazBottomNav(
                navController = navController,
                language = language
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier
                .padding(bottom = innerPadding.calculateBottomPadding())
                .background(MaterialTheme.colorScheme.background),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            composable(Screen.Home.route) {
                HomeScreen(viewModel = viewModel)
            }
            composable(Screen.Calendar.route) {
                CalendarScreen(
                    viewModel = viewModel,
                    onOpenReport = { navController.navigate("salat_report") }
                )
            }
            composable(Screen.Events.route) {
                EventsScreen(
                    viewModel = viewModel,
                    onOpenReport = { navController.navigate("event_report") }
                )
            }
            composable(Screen.Notes.route) {
                NotesScreen(
                    viewModel = viewModel,
                    onOpenReport = { navController.navigate("note_report") }
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    viewModel = viewModel,
                    onReplayOnboarding = { isReplayingOnboarding = true }
                )
            }
            composable(
                route = "salat_report",
                enterTransition = {
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(200)) + fadeIn(tween(200))
                },
                exitTransition = {
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(200)) + fadeOut(tween(200))
                },
                popEnterTransition = {
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(200)) + fadeIn(tween(200))
                },
                popExitTransition = {
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(200)) + fadeOut(tween(200))
                }
            ) {
                SalatReportScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(
                route = "event_report",
                enterTransition = {
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(200)) + fadeIn(tween(200))
                },
                exitTransition = {
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(200)) + fadeOut(tween(200))
                },
                popEnterTransition = {
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(200)) + fadeIn(tween(200))
                },
                popExitTransition = {
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(200)) + fadeOut(tween(200))
                }
            ) {
                EventReportScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(
                route = "note_report",
                enterTransition = {
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(200)) + fadeIn(tween(200))
                },
                exitTransition = {
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(200)) + fadeOut(tween(200))
                },
                popEnterTransition = {
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(200)) + fadeIn(tween(200))
                },
                popExitTransition = {
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(200)) + fadeOut(tween(200))
                }
            ) {
                ReflectionReportScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }

    // --- Global Modals & Dialogs ---

    // Delete confirmation dialog (Animated with spring scale + fade)
    AnimatedDeleteConfirmationDialog(
        isOpen = deleteDialogState.isOpen,
        target = deleteDialogState.target,
        language = language,
        onDismiss = { viewModel.dismissDeleteDialog() },
        onConfirm = { viewModel.confirmDelete() }
    )

    // Quick Prayer Status Bottom Sheet
    if (activePrayerPicker != null) {
        val (prayer, dateStr) = activePrayerPicker!!
        val record = prayerRecords[dateStr]
        val currentStatus = record?.prayers?.get(prayer) ?: PrayerStatus.NOT_YET
        val currentNote = record?.prayerNotes?.get(prayer)

        QuickPrayerStatusBottomSheet(
            prayer = prayer,
            dateStr = dateStr,
            currentStatus = currentStatus,
            currentNote = currentNote,
            language = language,
            onStatusSelected = { status ->
                viewModel.setPrayerStatus(prayer, status, dateStr)
            },
            onNoteSaved = { note ->
                viewModel.setPrayerNote(prayer, note, dateStr)
            },
            onDismiss = { viewModel.closePrayerStatusPicker() }
        )
    }

    // Add / Edit Event Bottom Sheet
    if (isEventEditorOpen) {
        AddEditEventBottomSheet(
            eventToEdit = editingEvent,
            language = language,
            onDismiss = { viewModel.closeEventEditor() },
            onSave = { titleEn, titleBn, dateStr, hijriEn, hijriBn, noteEn, noteBn, isRec, cat ->
                viewModel.saveEvent(titleEn, titleBn, dateStr, hijriEn, hijriBn, noteEn, noteBn, isRec, cat)
            }
        )
    }

    // Add / Edit Note Bottom Sheet
    if (isNoteEditorOpen) {
        AddEditNoteBottomSheet(
            noteToEdit = editingNote,
            language = language,
            onDismiss = { viewModel.closeNoteEditor() },
            onSave = { title, content, dateStr, linkedPrayer, tags ->
                viewModel.saveNote(title, content, dateStr, linkedPrayer, tags)
            }
        )
    }
}

@Composable
fun NamazBottomNav(
    navController: NavHostController,
    language: AppLanguage
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val screens = listOf(
        Screen.Home,
        Screen.Calendar,
        Screen.Events,
        Screen.Notes,
        Screen.Profile
    )

    Surface(
        modifier = Modifier
            .shadow(12.dp, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
        color = MaterialTheme.colorScheme.surface
    ) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp
        ) {
            screens.forEach { screen ->
                val selected = when (screen) {
                    Screen.Home -> currentRoute == Screen.Home.route
                    Screen.Calendar -> currentRoute == Screen.Calendar.route || currentRoute == "salat_report"
                    Screen.Events -> currentRoute == Screen.Events.route || currentRoute == "event_report"
                    Screen.Notes -> currentRoute == Screen.Notes.route || currentRoute == "note_report"
                    Screen.Profile -> currentRoute == Screen.Profile.route
                }

                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = if (selected) screen.selectedIcon else screen.unselectedIcon,
                            contentDescription = screen.labelProvider(language),
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = {
                        Text(
                            text = screen.labelProvider(language),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 11.sp
                            )
                        )
                    },
                    selected = selected,
                    onClick = {
                        if (currentRoute == screen.route) return@NavigationBarItem

                        val isSubRouteOfThisTab = when (screen) {
                            Screen.Calendar -> currentRoute == "salat_report"
                            Screen.Events -> currentRoute == "event_report"
                            Screen.Notes -> currentRoute == "note_report"
                            else -> false
                        }

                        if (isSubRouteOfThisTab) {
                            navController.popBackStack(screen.route, inclusive = false)
                        } else {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = false
                                }
                                launchSingleTop = true
                                restoreState = false
                            }
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ClayBrownPrimary,
                        selectedTextColor = ClayBrownPrimary,
                        indicatorColor = SoftButterAccent,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}
