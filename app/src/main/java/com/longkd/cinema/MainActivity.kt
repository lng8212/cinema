package com.longkd.cinema

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import com.longkd.cinema.core.fragment.ToolbarConfiguration
import com.longkd.cinema.customviews.CustomToolbar
import com.longkd.cinema.databinding.ActivityMainBinding
import com.longkd.cinema.notification.daily.ReminderManager
import com.longkd.cinema.utils.ENGLISH
import com.longkd.cinema.utils.ProgressDialog
import com.longkd.cinema.utils.navigateSafe
import com.longkd.cinema.utils.setAppLocale
import com.longkd.cinema.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController

    private val binding by viewBinding(ActivityMainBinding::inflate)

    private val viewModel by viewModels<MainViewModel>()

    lateinit var myIntent: Intent

    private var currentLanguage = ENGLISH

    private var progressDialog = ProgressDialog(this)

    private lateinit var auth: FirebaseAuth


    companion object {
        const val REMINDER_CHANNEL_NAME = "REMINDER_CHANNEL"
        const val REMINDER_CHANNEL_ID = "REMINDER_CHANNEL_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        installSplashScreen()
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary_dark)
        myIntent = intent
        currentLanguage = viewModel.getCurrentLocale()
        setAppLocale(currentLanguage)
        setContentView(binding.root)
        viewModel.getSetImagesConfigData()

        //Setting genre list from asset
        getGenresList(this, currentLanguage)?.let {
            GenresData.genres = it
        }
        setupNavigation()
        handleOnBoarding()
        createNotificationsChannels()
        handleNotification()
    }

    private fun handleNotification() {
        if (viewModel.getSwitchNotification()) {
            Log.e("xxxxxx", "handleNotification: here", )
            ReminderManager.startReminder(this)
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ContextWrapper(newBase?.setAppLocale(currentLanguage)))
    }

    private fun createNotificationsChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                REMINDER_CHANNEL_ID,
                REMINDER_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            ContextCompat.getSystemService(this, NotificationManager::class.java)
                ?.createNotificationChannel(channel)
        }
    }

    private fun handleOnBoarding() {
        if (viewModel.getShouldShowOnBoarding()) {
            val graph = navController.navInflater.inflate(R.navigation.main_navigation)
            graph.setStartDestination(R.id.onboardingFragment)
            navController.setGraph(graph, intent.extras)
        }
    }

    private fun setupNavigation() {
        navController =
            (supportFragmentManager.findFragmentById(binding.navigationHostFragment.id) as NavHostFragment).navController
        binding.bottomNavigationView.apply {
            setupWithNavController(navController)
        }
    }

    fun setStartDestinationToIntro() {
        val graph = navController.navInflater.inflate(R.navigation.main_navigation)
        graph.setStartDestination(R.id.introFragment)
        navController.setGraph(graph, intent.extras)
    }

    fun setStartDestinationToHome() {
        val graph = navController.navInflater.inflate(R.navigation.main_navigation)
        graph.setStartDestination(R.id.homeFragment)
        navController.setGraph(graph, intent.extras)
    }

    fun navBack() {
        navController.navigateUp()
    }

    fun nav(directions: NavDirections, onError: (() -> Unit)? = null) {
        navController.navigateSafe(directions, onError)
    }

    fun configureToolbar(toolbarConfiguration: ToolbarConfiguration?) {
        binding.toolbar.configure(toolbarConfiguration)
    }

    fun getToolbar(): CustomToolbar = binding.toolbar

    fun hideToolbar() {
        binding.toolbar.visibility = View.GONE
    }

    fun showToolbar() {
        binding.toolbar.visibility = View.VISIBLE
    }

    fun hideBottomNavbar() {
        binding.bottomNavigationView.visibility = View.GONE
    }

    fun showBottomNavBar() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }

    fun showProgressDialog() {
        progressDialog.startDialog()
    }

    fun hideProgressDialog() {
        progressDialog.dismissDialog()
    }

    fun restartActivity() {
        finish()
        startActivity(myIntent)
    }

}
