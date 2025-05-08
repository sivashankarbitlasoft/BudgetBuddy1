package com.example.budgetbuddy1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.budgetbuddy1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(){


    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarLayout)
        navController = findNavController(R.id.nav_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.overviewFragment,
                R.id.transactionsFragment,
                R.id.budgetFragment)
            ,binding.drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)

        //binding.sideNav.setupWithNavController(navController)

        binding.bottomNavigationView.setupWithNavController(navController)
        binding.sideNav.setNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.settingsFragment -> {
                    navController.navigate(R.id.settingsFragment)
                }
                R.id.aboutUsFragment -> {
                    navController.navigate(R.id.aboutUsFragment)
                }
                R.id.nav_logout -> {

                }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
        //
//        binding.sideNav.setNavigationItemSelectedListener(this)
//
//        val toggle = ActionBarDrawerToggle(this,binding.drawerLayout, binding.appBarLayout, R.string.open_nav, R.string.close_nav)
//        binding.drawerLayout.addDrawerListener(toggle)
//        toggle.syncState()


        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

//    override fun onNavigationItemSelected(item: MenuItem): Boolean{
//        binding.drawerLayout.closeDrawer(GravityCompat.START)
//        return true
//    }
//
////    private fun replaceFragment(fragment: Fragment){
////        val fragmentManager = supportFragmentManager
////        val fragmentTransaction = fragmentManager.beginTransaction()
////        fragmentTransaction.replace(R.id.frame_layout, fragment)
////        fragmentTransaction.commit()
////    }
//
//    override fun onBackPressed() {
//        super.onBackPressed()
//        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
//            binding.drawerLayout.closeDrawer(GravityCompat.START)
//        }
//        else{
//            onBackPressedDispatcher.onBackPressed()
//        }
//    }
//
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}