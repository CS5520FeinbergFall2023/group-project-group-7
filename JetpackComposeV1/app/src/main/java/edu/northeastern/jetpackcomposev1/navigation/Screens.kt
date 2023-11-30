package edu.northeastern.jetpackcomposev1.navigation

sealed class Screens(val route:String) {
    object ForgotPasswordScreen: Screens("forgot_password")
    object SignInScreen: Screens("sign_in")
    object SignUpScreen: Screens("sign_up")
    object JobApplicationScreen: Screens("job_application")
    object JobDetailScreen: Screens("job_detail")
    object JobSearchScreen: Screens("job_search")
    object JobFavoriteScreen: Screens("job_favorite")
    object LaunchScreen: Screens("launch")
    object ProfileScreen: Screens("profile")
    object ResumesScreen: Screens("resumes")
    object SettingsScreen: Screens("settings")
    object EventUpdateScreen: Screens("event_update")
    object HomeScreen: Screens("home")
    object MyApp: Screens("my_app")
    object ApplicationUpdateScreen: Screens("application_update")
    object CreateNewApplicationScreen: Screens("create_new_application")
    object ApplicationDetailScreen: Screens("application_detail")


}