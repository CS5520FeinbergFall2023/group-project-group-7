package edu.northeastern.jetpackcomposev1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import edu.northeastern.jetpackcomposev1.models.AccountViewModel
import edu.northeastern.jetpackcomposev1.screens.ForgotPasswordScreen
import edu.northeastern.jetpackcomposev1.screens.JobAppliedScreen
import edu.northeastern.jetpackcomposev1.screens.JobSavedScreen
import edu.northeastern.jetpackcomposev1.screens.JobSearchScreen
import edu.northeastern.jetpackcomposev1.screens.LaunchScreen


import edu.northeastern.jetpackcomposev1.screens.SignInScreen
import edu.northeastern.jetpackcomposev1.screens.SignUpScreen

import edu.northeastern.jetpackcomposev1.ui.theme.JetpackComposeV1Theme
import kotlinx.coroutines.launch

data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

val items: List<NavigationItem> = listOf(
    NavigationItem(
        title = "Search",
        icon = Icons.Outlined.Search,
        route = "SearchJobs"
    ),
    NavigationItem(
        title = "Saved",
        icon = Icons.Outlined.List,
        route = "SavedJobs"
    ),
    NavigationItem(
        title = "Applied",
        icon = Icons.Outlined.List,
        route = "AppliedJobs"
    ),
    NavigationItem(
        title = "Resumes",
        icon = Icons.Outlined.List,
        route = "Resumes"
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
    )
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
    val accountViewModel: AccountViewModel = viewModel()
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Launch") {
        composable("Launch") {
            LaunchScreen(
                accountViewModel = accountViewModel,
                onNavigateToHome = { navController.navigate("Home") { popUpTo("Launch") {inclusive = true} } },
                onNavigateToSignIn = { navController.navigate("SignIn") { popUpTo("Launch") {inclusive = true} } }
            ) }
        composable("SignIn") {
            SignInScreen(
                accountViewModel = accountViewModel,
                onNavigateToSignUp = { navController.navigate("SignUp") { popUpTo("SignIn") {inclusive = true} } },
                onNavigateToForgotPassword = { navController.navigate("ForgotPassword") { popUpTo("SignIn") {inclusive = true} } },
                onNavigateToHome = { navController.navigate("Home") { popUpTo("SignIn") {inclusive = true} } }
            )
        }
        composable("SignUp") {
            SignUpScreen(
                accountViewModel = accountViewModel,
                onNavigateToSignIn = { navController.navigate("SignIn") { popUpTo("SignUp") {inclusive = true} } },
                onNavigateToForgotPassword = { navController.navigate("ForgotPassword") { popUpTo("SignUp") {inclusive = true} } },
                onNavigateToHome = { navController.navigate("Home") { popUpTo("SignUp") {inclusive = true} } }
            )
        }
        composable("ForgotPassword") {
            ForgotPasswordScreen(
                accountViewModel = accountViewModel,
                onNavigateToSignIn = { navController.navigate("SignIn") { popUpTo("ForgotPassword") {inclusive = true} } },
                onNavigateToSignUp = { navController.navigate("SignUp") { popUpTo("ForgotPassword") {inclusive = true} } }
            )
        }
        composable("Home") {
            HomeScreen(
                accountViewModel = accountViewModel,
                onNavigateToSignIn = { navController.navigate("SignIn") { popUpTo("Home") {inclusive = true} } }
            )
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
    accountViewModel: AccountViewModel,
    onNavigateToSignIn: () -> Unit
) {
    // define nav controller
    val navController = rememberNavController()
    // add navigation drawer here
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(300.dp)) {
                LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
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
                        Text(text = "Your name here", modifier = Modifier.padding(12.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    // sheet body
                    itemsIndexed(items) {index, item ->
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
                    // sheet foot
                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                        Divider()
                        TextButton(onClick = { accountViewModel.signOut() }) {
                            Text(text = "Sign Out")
                        }
                    }
                }
            }
        },
        drawerState = drawerState,
        gesturesEnabled = false
    ) {
        // add screen content here
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Job Track Pro") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    }
                )
            }
        ) {
            contentPadding ->
            Column(
                modifier = Modifier.padding(contentPadding)
            ) {
                NavHost(navController = navController, startDestination = "SearchJobs") {
                    composable("SearchJobs") { JobSearchScreen() }
                    composable("SavedJobs") { JobSavedScreen() }
                    composable("AppliedJobs") { JobAppliedScreen() }
                }
            }
        }
    }
    if (!accountViewModel.isSignedIn) {
        onNavigateToSignIn()
    }
//    Log.d("debug", "Home render finished")
}
