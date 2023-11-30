package edu.northeastern.jetpackcomposev1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.northeastern.jetpackcomposev1.application.ApplicationDetailScreen
import edu.northeastern.jetpackcomposev1.navigation.Screens
import edu.northeastern.jetpackcomposev1.ui.screens.ApplicationUpdateScreen
import edu.northeastern.jetpackcomposev1.ui.screens.CreateNewApplicationScreen
import edu.northeastern.jetpackcomposev1.viewmodels.UserViewModel
import edu.northeastern.jetpackcomposev1.viewmodels.JobViewModel
import edu.northeastern.jetpackcomposev1.ui.screens.ForgotPasswordScreen
import edu.northeastern.jetpackcomposev1.ui.screens.JobApplicationScreen
import edu.northeastern.jetpackcomposev1.ui.screens.JobDetailScreen
import edu.northeastern.jetpackcomposev1.ui.screens.JobFavoriteScreen
import edu.northeastern.jetpackcomposev1.ui.screens.JobSearchScreen
import edu.northeastern.jetpackcomposev1.ui.screens.LaunchScreen
import edu.northeastern.jetpackcomposev1.ui.screens.ProfileScreen
import edu.northeastern.jetpackcomposev1.ui.screens.ResumesScreen
import edu.northeastern.jetpackcomposev1.ui.sheets.SearchJobSheet
import edu.northeastern.jetpackcomposev1.ui.screens.SettingsScreen


import edu.northeastern.jetpackcomposev1.ui.screens.SignInScreen
import edu.northeastern.jetpackcomposev1.ui.screens.SignUpScreen
import edu.northeastern.jetpackcomposev1.ui.sheets.EventUpdateScreen

import edu.northeastern.jetpackcomposev1.ui.theme.JetpackComposeV1Theme
import edu.northeastern.jetpackcomposev1.viewmodels.ApplicationViewModel
import edu.northeastern.jetpackcomposev1.viewmodels.ResumeViewModel
import kotlinx.coroutines.launch

data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

val navItems: List<NavigationItem> = listOf(
    NavigationItem(
        title = "Job Search",
        icon = Icons.Outlined.Search,
        route = Screens.JobSearchScreen.route
    ),
    NavigationItem(
        title = "My Favorites",
        icon = Icons.Outlined.List,
        route = Screens.JobFavoriteScreen.route
    ),
    NavigationItem(
        title = "My Applications",
        icon = Icons.Outlined.List,
        route = Screens.JobApplicationScreen.route
    ),
    NavigationItem(
        title = "My Resumes",
        icon = Icons.Outlined.List,
        route = Screens.ResumesScreen.route
    ),
    NavigationItem(
        title = "Profile",
        icon = Icons.Outlined.Person,
        route = Screens.ProfileScreen.route
    ),
    NavigationItem(
        title = "Settings",
        icon = Icons.Outlined.Settings,
        route = Screens.SettingsScreen.route
    ),
)


class MainActivity : ComponentActivity() {
//    val accountViewModel by viewModels<AccountViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeV1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    // similar to layout_height="match_parent", layout_width="match_parent"
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp()
                }
            }
        }
    }
}

@Composable
fun MyApp() {
    // create our viewModels first to store data
    val userViewModel: UserViewModel = viewModel()
    val jobViewModel: JobViewModel = viewModel()
    val applicationViewModel: ApplicationViewModel = viewModel()
    val resumeViewModel: ResumeViewModel = viewModel()

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.LaunchScreen.route) {
        composable(Screens.LaunchScreen.route) {
            LaunchScreen(
                userViewModel = userViewModel,
                onNavigateToHome = { navController.navigate(Screens.HomeScreen.route) { popUpTo(navController.currentBackStackEntry?.destination?.route.toString()) {inclusive = true} } },
                onNavigateToSignIn = { navController.navigate(Screens.SignInScreen.route) { popUpTo(navController.currentBackStackEntry?.destination?.route.toString()) {inclusive = true} } }
            ) }
        composable(Screens.SignInScreen.route) {
            SignInScreen(
                userViewModel = userViewModel,
                onNavigateToSignUp = { navController.navigate(Screens.SignUpScreen.route) { popUpTo(navController.currentBackStackEntry?.destination?.route.toString()) {inclusive = true} } },
                onNavigateToForgotPassword = { navController.navigate(Screens.ForgotPasswordScreen.route) { popUpTo(navController.currentBackStackEntry?.destination?.route.toString()) {inclusive = true} } },
                onNavigateToHome = { navController.navigate(Screens.HomeScreen.route) { popUpTo(navController.currentBackStackEntry?.destination?.route.toString()) {inclusive = true} } }
            )
        }
        composable(Screens.SignUpScreen.route) {
            SignUpScreen(
                userViewModel = userViewModel,
                onNavigateToSignIn = { navController.navigate(Screens.SignInScreen.route) { popUpTo(navController.currentBackStackEntry?.destination?.route.toString()) {inclusive = true} } },
                onNavigateToForgotPassword = { navController.navigate(Screens.ForgotPasswordScreen.route) { popUpTo(navController.currentBackStackEntry?.destination?.route.toString()) {inclusive = true} } },
                onNavigateToHome = { navController.navigate(Screens.HomeScreen.route) { popUpTo(navController.currentBackStackEntry?.destination?.route.toString()) {inclusive = true} } }
            )
        }
        composable(Screens.ForgotPasswordScreen.route) {
            ForgotPasswordScreen(
                userViewModel = userViewModel,
                onNavigateToSignIn = { navController.navigate(Screens.SignInScreen.route) { popUpTo(navController.currentBackStackEntry?.destination?.route.toString()) {inclusive = true} } },
                onNavigateToSignUp = { navController.navigate(Screens.SignUpScreen.route) { popUpTo(navController.currentBackStackEntry?.destination?.route.toString()) {inclusive = true} } }
            )
        }
        composable(Screens.HomeScreen.route) {
            HomeScreen(
                userViewModel = userViewModel,
                jobViewModel = jobViewModel,
                applicationViewModel = applicationViewModel,
                resumeViewModel = resumeViewModel,
                onNavigateToMyApp = { navController.navigate(Screens.MyApp.route) { popUpTo(navController.currentBackStackEntry?.destination?.route.toString()) {inclusive = true} } }
            )
        }
        composable(Screens.MyApp.route) {
            MyApp()
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewMyApp() {
//    JetpackComposeV1Theme {
//        MyApp()
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userViewModel: UserViewModel,
    jobViewModel: JobViewModel,
    applicationViewModel: ApplicationViewModel,
    resumeViewModel: ResumeViewModel,
    onNavigateToMyApp: () -> Unit
) {
    // fetch data from DB
    val runOnlyOnce = true
    LaunchedEffect(key1 = runOnlyOnce) {
        jobViewModel.getJobFromAPI()
        jobViewModel.getJobSearchHistoryFromDB()
        jobViewModel.getJobViewedHistoryFromDB()
        jobViewModel.getJobFavoriteFromDB()
        applicationViewModel.getJobApplicationFromDB()
        resumeViewModel.getResumeFromDB()
    }
    // define nav controller
    val navController = rememberNavController()
    // add navigation drawer here
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(300.dp)) {
                LazyColumn {
                    // sheet head
                    item {
                        Image(
                            painter = painterResource(R.drawable.ic_launcher_background),
                            contentDescription = "Avatar",
                            modifier = Modifier
                                // Set image size to 40 dp
                                .size(40.dp)
                                // Clip image to be shaped as a circle
                                .clip(CircleShape)
                                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        )
                        Text(text = userViewModel.user.profile.name, modifier = Modifier.padding(12.dp))
                    }
                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    // sheet body
                    itemsIndexed(navItems) {index, item ->
                        NavigationDrawerItem(
                            label = { Text(text = item.title) },
                            selected = (index == selectedItemIndex),
                            onClick = {
                                selectedItemIndex = index
                                scope.launch { drawerState.close() }
                                navController.navigate(item.route) { popUpTo(navController.currentBackStackEntry?.destination?.route.toString()) {inclusive = true} }
                            },
                            icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    // sheet foot
                    item {
                        NavigationDrawerItem(
                            label = { Text("Sign Out") },
                            selected = false,
                            onClick = {
                                scope.launch { drawerState.close() }
                                userViewModel.signOut()
                            },
                            icon = { Icon(imageVector = Icons.Outlined.ExitToApp, contentDescription = "Sign Out") },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            }
        },
        drawerState = drawerState,
        gesturesEnabled = true
    ) {
        // add screen content here
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Job Track Pro") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    },
                    colors = topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
                )
            }
        ) {
            contentPadding ->
            Column(
                modifier = Modifier.padding(contentPadding)
            ) {
                NavHost(navController = navController, startDestination = "Job_Search") {
                    composable(Screens.JobSearchScreen.route) {
                        JobSearchScreen(
                            jobViewModel, applicationViewModel,
                            onNavigateToJobDetail = {
                                navController.navigate(Screens.JobDetailScreen.route) {
                                    popUpTo(navController.currentBackStackEntry?.destination?.route.toString()) {
                                        inclusive = true
                                    }
                                }
                            },
                        )
                    }
                    composable(Screens.JobFavoriteScreen.route) {
                        JobFavoriteScreen(
                            jobViewModel,
                            applicationViewModel,
                            onNavigateToJobDetail = {
                                navController.navigate(Screens.JobDetailScreen.route) {
                                    popUpTo(navController.currentBackStackEntry?.destination?.route.toString()) {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }
                    composable(Screens.JobDetailScreen.route) {
                        JobDetailScreen(
                            jobViewModel = jobViewModel,
                            onNavigateToApply = {
                                //TOdo: Replace the navigate to apply screen
                                navController.navigate(Screens.JobApplicationScreen.route) {
                                    popUpTo(navController.currentBackStackEntry?.destination?.route.toString()) {
                                        inclusive = true
                                    }
                                }
                            },
                            onNavigateToApplicationUpdate = {
                                navController.navigate(Screens.CreateNewApplicationScreen.route) {
                                    popUpTo(navController.currentBackStackEntry?.destination?.route.toString()) {
                                        inclusive = true
                                    }
                                }
                            })
                    }
                    composable(Screens.JobApplicationScreen.route) {
                        JobApplicationScreen(
                            applicationViewModel,
                            onNavigateToApplicationDetail = {
                                navController.navigate(Screens.ApplicationDetailScreen.route) {
                                    popUpTo(navController.currentBackStackEntry?.destination?.route.toString()) {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }
                    composable(Screens.ApplicationUpdateScreen.route) {
                        ApplicationUpdateScreen(jobViewModel, applicationViewModel,onCancel={
                            navController.navigateUp()
                        }, onNext={
                            navController.navigate(Screens.EventUpdateScreen.route) {
                                popUpTo(navController.currentBackStackEntry?.destination?.route.toString()) {
                                    inclusive = true
                                }
                            }
                        })

                    }
                    composable(Screens.CreateNewApplicationScreen.route){
                        CreateNewApplicationScreen(jobViewModel, applicationViewModel, resumeViewModel, onNavigateToApplicationDetail = {
                            navController.navigate(Screens.ApplicationDetailScreen.route) {
                                popUpTo(navController.currentBackStackEntry?.destination?.route.toString()) {
                                    inclusive = true
                                }
                            }
                        })
                    }
                    composable(Screens.ApplicationDetailScreen.route) {
                        ApplicationDetailScreen(
                            jobViewModel,
                            resumeViewModel,
                            applicationViewModel,
                            onNavigateToEventUpdate = {
                                navController.navigate(Screens.EventUpdateScreen.route) {
                                    popUpTo(navController.currentBackStackEntry?.destination?.route.toString()) {
                                        inclusive = true
                                    }
                                }
                            },
                            onNavigateToJobDetail = {
                                navController.navigate(Screens.JobDetailScreen.route) {
                                    popUpTo(navController.currentBackStackEntry?.destination?.route.toString()) {
                                        inclusive = true
                                    }
                                }
                            },
                            onNavigateToResumeDetail = {
                                navController.navigate(Screens.ResumesScreen.route) {
                                    popUpTo(navController.currentBackStackEntry?.destination?.route.toString()) {
                                        inclusive = true
                                    }
                                }
                            })
                    }
                    composable(Screens.EventUpdateScreen.route) {
                        EventUpdateScreen(applicationViewModel = applicationViewModel,
                            onNavigateToApplicationDetail = {
                            navController.navigate(Screens.ApplicationDetailScreen.route) {
                                popUpTo(navController.currentBackStackEntry?.destination?.route.toString()) {
                                    inclusive = true
                                }
                            }
                        }, onCancel={
                            navController.navigate(Screens.ApplicationDetailScreen.route) {
                                popUpTo(navController.currentBackStackEntry?.destination?.route.toString()) {
                                    inclusive = true
                                }
                            }
                        })

                    }
                    composable(Screens.ResumesScreen.route) { ResumesScreen(resumeViewModel) }
                    composable(Screens.ProfileScreen.route) { ProfileScreen() }
                    composable(Screens.SettingsScreen.route) { SettingsScreen() }

                }
            }
        }
    }
    if (!userViewModel.isSignedIn) {
        onNavigateToMyApp()
    }
//    Log.d("debug", "Home render finished")
}
