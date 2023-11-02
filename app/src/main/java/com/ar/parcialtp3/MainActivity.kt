package com.ar.parcialtp3

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navController: NavController
    private lateinit var navigationView: NavigationView

    //Header
    lateinit var drawerHeader: View
    lateinit var imgHeader: ImageView
    lateinit var nameHeader: TextView

    //Shared
    lateinit var sharedPreferences: SharedPreferences

    //Bottom nav
    lateinit var bottomNavigation: BottomNavigationView

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigationView = findViewById(R.id.nav_view)
        navigationView.bringToFront()

        //Shared
        sharedPreferences = this.getSharedPreferences("my_preference", Context.MODE_PRIVATE)

        //Header drawer

        drawerHeader = navigationView.getHeaderView(0)
        imgHeader = drawerHeader.findViewById(R.id.img_header)
        nameHeader = drawerHeader.findViewById(R.id.txt_name_header)

        nameHeader.text = sharedPreferences.getString("username", "")
        val imageUrl = sharedPreferences.getString("image", "")
        Glide.with(this).load(imageUrl).into(imgHeader);

        //Drawer menu

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.mainFragmentContainer) as NavHostFragment
        navController = navHostFragment.navController

        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_open, R.string.nav_close)

        drawer.addDrawerListener(toggle)

        actionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profileFragment -> {
                    Log.d("Navigation", "Profile Fragment Clicked")
                    navController.navigate(R.id.profileFragment)
                }

                R.id.settingsFragment -> {
                    Log.d("Navigation", "Settings Fragment Clicked")
                    navController.navigate(R.id.settingsFragment)
                }
            }

            drawer.closeDrawer(GravityCompat.START)
            true
        }

        //Bottom nav

        bottomNavigation = findViewById(R.id.bottom_nav_view)

        NavigationUI.setupWithNavController(bottomNavigation, navHostFragment.navController)

        bottomNavigation.setOnItemSelectedListener {item ->
            when(item.itemId){
                R.id.homeFragment ->{
                    navHostFragment.navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.favouritesFragment -> {
                    navHostFragment.navController.navigate(R.id.favouritesFragment)
                    true
                }
                R.id.adoptionsListFragment -> {
                    navHostFragment.navController.navigate(R.id.adoptionsListFragment)
                    true
                }
                R.id.publishFragment -> {
                    navHostFragment.navController.navigate(R.id.publishFragment)
                    true
                }
                else -> false
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawer)
        // enabling action bar app icon and behaving it as a toggle button
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(true)
    }

//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//
//    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.drawer_menu, menu)
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)

    }

}