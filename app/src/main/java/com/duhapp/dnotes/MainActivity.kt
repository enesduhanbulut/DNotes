package com.duhapp.dnotes

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.duhapp.dnotes.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.bottomNavView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_manage_category,
                R.id.navigation_notifications,
            ),
        )
        setSupportActionBar(binding.toolbar)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.navigation_note || destination.id == R.id.navigation_manage_category) {
                setBottomAppBarVisibility(View.GONE)
            } else {
                setBottomAppBarVisibility(View.VISIBLE)
            }
        }

        binding.fab.setOnClickListener {
            navController.navigate(
                R.id.navigation_note,
            )
        }
        setAppBarVisibility(View.GONE)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        viewModel.initializeDefaultDataModels()
    }

    private fun setBottomAppBarVisibility(visibility: Int) {
        binding.bottomNavView.visibility = visibility
        binding.fab.visibility = visibility
    }

    private fun setAppBarVisibility(visibility: Int) {
        binding.toolbar.visibility = visibility
    }

    fun setAppBarTitle(@StringRes title: Int) {
        binding.toolbar.title = getString(title)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun setFabVisibility(visibility: Int) {
        binding.fab.visibility = visibility
    }
}
