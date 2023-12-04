package edu.northeastern.jetpackcomposev1

import android.os.Bundle
import android.util.Log
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
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDrawerState
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import edu.northeastern.jetpackcomposev1.viewmodels.UserViewModel
import edu.northeastern.jetpackcomposev1.viewmodels.JobViewModel
import edu.northeastern.jetpackcomposev1.ui.screens.ForgotPasswordScreen
import edu.northeastern.jetpackcomposev1.ui.screens.JobApplicationScreen
import edu.northeastern.jetpackcomposev1.ui.screens.JobDetailScreen
import edu.northeastern.jetpackcomposev1.ui.screens.JobFavoriteScreen
import edu.northeastern.jetpackcomposev1.ui.screens.JobSearchScreen
import edu.northeastern.jetpackcomposev1.ui.screens.LaunchScreen
import edu.northeastern.jetpackcomposev1.ui.screens.PDFViewScreen
import edu.northeastern.jetpackcomposev1.ui.screens.ProfileScreen
import edu.northeastern.jetpackcomposev1.ui.screens.ResumesScreen
import edu.northeastern.jetpackcomposev1.ui.screens.SettingsScreen
import edu.northeastern.jetpackcomposev1.ui.screens.AddNewApplicationScreen

import edu.northeastern.jetpackcomposev1.ui.screens.SignInScreen
import edu.northeastern.jetpackcomposev1.ui.screens.SignUpScreen

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
        route = "Job_Search"
    ),
    NavigationItem(
        title = "My Favorites",
        icon = Icons.Outlined.List,
        route = "My_Favorites"
    ),
    NavigationItem(
        title = "My Applications",
        icon = Icons.Outlined.List,
        route = "My_Applications"
    ),
    NavigationItem(
        title = "My Resumes",
        icon = Icons.Outlined.List,
        route = "My_Resumes"
    ),
    NavigationItem(
        title = "Profile",
        icon = Icons.Outlined.Person,
        route = "Profile"
    ),
    NavigationItem(
        title = "Settings",
        icon = Icons.Outlined.Settings,
        route = "Settings"
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
    NavHost(navController = navController, startDestination = "Launch") {
        composable("Launch") {
            LaunchScreen(
                userViewModel = userViewModel,
                onNavigateToHome = {
                    navController.navigate("Home") {
                        popUpTo(navController.currentBackStackEntry?.destination?.route!!) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToSignIn = {
                    navController.navigate("Sign_In") {
                        popUpTo(navController.currentBackStackEntry?.destination?.route!!) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable("Sign_In") {
            SignInScreen(
                userViewModel = userViewModel,
                onNavigateToSignUp = {
                    navController.navigate("Sign_Up") {
                        popUpTo(navController.currentBackStackEntry?.destination?.route!!) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToForgotPassword = {
                    navController.navigate("Forgot_Password") {
                        popUpTo(
                            navController.currentBackStackEntry?.destination?.route!!
                        ) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate("Home") {
                        popUpTo(navController.currentBackStackEntry?.destination?.route!!) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable("Sign_Up") {
            SignUpScreen(
                userViewModel = userViewModel,
                onNavigateToSignIn = {
                    navController.navigate("Sign_In") {
                        popUpTo(navController.currentBackStackEntry?.destination?.route!!) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToForgotPassword = {
                    navController.navigate("Forgot_Password") {
                        popUpTo(
                            navController.currentBackStackEntry?.destination?.route!!
                        ) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate("Home") {
                        popUpTo(navController.currentBackStackEntry?.destination?.route!!) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable("Forgot_Password") {
            ForgotPasswordScreen(
                userViewModel = userViewModel,
                onNavigateToSignIn = {
                    navController.navigate("Sign_In") {
                        popUpTo(navController.currentBackStackEntry?.destination?.route!!) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate("Sign_Up") {
                        popUpTo(navController.currentBackStackEntry?.destination?.route!!) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable("Home") {
            HomeScreen(
                userViewModel = userViewModel,
                jobViewModel = jobViewModel,
                applicationViewModel = applicationViewModel,
                resumeViewModel = resumeViewModel,
                onNavigateToMyApp = {
                    navController.navigate("My_App") {
                        popUpTo(navController.currentBackStackEntry?.destination?.route!!) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable("My_App") {
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
    if(userViewModel.firstLaunch) {
        jobViewModel.getJobFromAPI()
        jobViewModel.getJobSearchHistoryFromDB()
        jobViewModel.getJobViewedHistoryFromDB()
        jobViewModel.getJobFavoriteFromDB()
        applicationViewModel.getJobApplicationFromDB()
        resumeViewModel.getResumeFromDB()
        userViewModel.firstLaunch = false
        Log.d("debug", "test how many runs!!!!")
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
                        Text(
                            text = userViewModel.user.profile.name,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    // sheet body
                    itemsIndexed(navItems) { index, item ->
                        NavigationDrawerItem(
                            label = { Text(text = item.title) },
                            selected = (index == selectedItemIndex),
                            onClick = {
                                selectedItemIndex = index
                                scope.launch { drawerState.close() }
                                navController.navigate(item.route) {
                                    popUpTo(navController.currentBackStackEntry?.destination?.route!!) {
                                        inclusive = true
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title
                                )
                            },
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
                            icon = {
                                Icon(
                                    imageVector = Icons.Outlined.ExitToApp,
                                    contentDescription = "Sign Out"
                                )
                            },
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
        ) { contentPadding ->
            Column(
                modifier = Modifier.padding(contentPadding)
            ) {
                NavHost(navController = navController, startDestination = "Job_Search") {
                    composable("Job_Search") {
                        JobSearchScreen(
                            jobViewModel = jobViewModel,
                            applicationViewModel = applicationViewModel,
                            onNavigateToJobDetail = { index -> navController.navigate("Job_Details/search/$index") }
                        )
                    }
                    composable("My_Favorites") {
                        JobFavoriteScreen(
                            jobViewModel = jobViewModel,
                            applicationViewModel = applicationViewModel,
                            onNavigateToJobDetail = { index -> navController.navigate("Job_Details/favorite/$index") }
                        )
                    }
                    composable("My_Applications") { JobApplicationScreen(applicationViewModel) }
                    composable("My_Resumes") {
                        ResumesScreen(
                            resumeViewModel,
                            navController = navController
                        )
                    }
                    composable("Profile") { ProfileScreen() }
                    composable("Settings") { SettingsScreen() }
                    composable("PDFViewScreen") {
                        PDFViewScreen(
                            viewModel = resumeViewModel,
                            onNavigateToResumeManagement = {
                                navController.navigate("My_Resumes") {
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }
                    composable("Job_Details/{listName}/{index}") { navBackStackEntry ->
                        val listName = navBackStackEntry.arguments?.getString("listName")
                        val index = navBackStackEntry.arguments?.getString("index")?.toInt()
                        if (listName != null && index != null) {
                            JobDetailScreen(listName = listName,
                                index = index,
                                jobViewModel = jobViewModel,
                                applicationViewModel = applicationViewModel,
                                onNavigateToApply = {
                                    navController.navigate("Add_New_Application/add") {
                                        /*popUpTo(navController.currentBackStackEntry?.destination?.route.toString()) {
                                            inclusive = true
                                        }*/
                                    }
                                })
                        }
                    }

                    /*
                    * For adding a new application:
                    * navController.navigate("Add_New_Application/add")
                    * For editing an existing application:
                    * navController.navigate("Add_New_Application/$applicationId")
                    */
                    composable(
                        route = "Add_New_Application/{applicationId}",
                        arguments = listOf(
                            navArgument("applicationId") {
                                // Set default value as an indication of 'add' mode
                                defaultValue = "add"
                                nullable = true // Make it nullable
                                type = NavType.StringType
                            }
                        )
                    ) { navBackStackEntry ->
                        // Extract the applicationId argument
                        val applicationId = navBackStackEntry.arguments?.getString("applicationId")

                        // Determine the mode based on whether an applicationId is provided
                        val isEditMode = applicationId != null && applicationId != "add"

                        AddNewApplicationScreen(
                            jobViewModel = jobViewModel,
                            applicationViewModel = applicationViewModel,
                            resumeViewModel = resumeViewModel,
                            navController = navController,
                            onNavigateToResume = {
                                navController.navigate("My_Resumes") {
                                    /*popUpTo(navController.graph.startDestinationId) {
                                        inclusive = true
                                    }*/
                                }
                            },
                            applicationId = if (isEditMode) applicationId else null
                        )
                    }
                }
            }
        }
    }
    if (!userViewModel.isSignedIn) {
        onNavigateToMyApp()
    }
//    Log.d("debug", "Home render finished")
}
