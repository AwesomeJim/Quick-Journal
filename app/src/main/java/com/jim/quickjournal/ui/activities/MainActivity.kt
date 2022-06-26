/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jim.quickjournal.ui.activities

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.jim.quickjournal.R
import com.jim.quickjournal.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    // var mAuth: FirebaseAuth? = null
//    var user: FirebaseUser? = null

    var loginMode: String? = null
    var loginDetails: String? = null
    var photo: CircleImageView? = null
    var photoUrl: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
        navController = findNavController(R.id.nav_host_fragment)

        //Floating Action Button for adding a Journal entry
        binding.appBarMain.fab.setOnClickListener {
            navController.navigate(R.id.action_nav_to_AddJournalFragment)
        }

        val drawer: DrawerLayout = binding.drawerLayout
        val navigationView: NavigationView = binding.navView

        val headerLayout = navigationView.getHeaderView(0)
        photo = headerLayout.findViewById(R.id.profile_image)
        val loginM = headerLayout.findViewById<TextView>(R.id.login_method)
        loginM.text = loginMode
        val details = headerLayout.findViewById<TextView>(R.id.user_details)
        details.text = loginDetails

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_profile, R.id.nav_help, R.id.nav_logout
            ), drawer
        )

        /* NavigationUI.setupActionBarWithNavController(this, navController, drawer)
         NavigationUI.setupWithNavController(
             binding.appBarMain.toolbar,
             navController,
             appBarConfiguration
         )*/
        setupActionBarWithNavController(navController, appBarConfiguration)
        navigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.homeFragment) {
                binding.appBarMain.fab.visibility = View.VISIBLE
                supportActionBar?.displayOptions =
                    ActionBar.DISPLAY_SHOW_TITLE or ActionBar.DISPLAY_SHOW_HOME
                supportActionBar?.setDisplayShowHomeEnabled(true)
                supportActionBar?.setDisplayShowTitleEnabled(true)
            } else {
                supportActionBar?.displayOptions =
                    ActionBar.DISPLAY_SHOW_TITLE or ActionBar.DISPLAY_HOME_AS_UP
                supportActionBar?.setHomeButtonEnabled(true)
                supportActionBar?.setDisplayShowTitleEnabled(true)
                binding.appBarMain.fab.visibility = View.GONE
            }
        }
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> {

                }
                else -> {
                    // Trigger the default action of replacing the current
                    // screen with the one matching the MenuItem's ID
                    onNavDestinationSelected(menuItem, navController)
                }

            }
            drawer.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            navController,
            appBarConfiguration
        ) || super.onSupportNavigateUp()
    }

    /**
     * Get the details of the Logged in User
     */
/*    private fun loadGoogleUserDetails() {
        */
    /**
     * Check which method/Provider a user Used to login
     *//*
        for (profile in user!!.providerData) {
            when (profile.providerId) {
                "google.com" -> {


                    // Name, email address, and profile photo Url
                    loginMode = profile.displayName
                    loginDetails = profile.email
                    photoUrl = profile.photoUrl
                }
                "firebase" -> {

                    // Name, email address, and profile photo Url if available
                    loginDetails = profile.email
                    loginMode = profile.displayName
                }
                "phone" -> {

                    // Name, email address, and profile photo Url if its available
                    loginDetails = profile.phoneNumber
                    loginMode = profile.providerId
                }
            }
        }
    }*/


    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}