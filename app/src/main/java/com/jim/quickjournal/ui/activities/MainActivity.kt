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
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.jim.quickjournal.R
import com.jim.quickjournal.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    // var mAuth: FirebaseAuth? = null
//    var user: FirebaseUser? = null
    var toolbar: Toolbar? = null
    var loginMode: String? = null
    var loginDetails: String? = null
    var photo: CircleImageView? = null
    var photoUrl: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        initViews()
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

    private fun initViews() {

        //Floating Action Button for adding a Journal entry
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            navController.navigate(R.id.action_nav_to_AddJournalFragment)
        }
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)


        val headerLayout = navigationView.getHeaderView(0)
        photo = headerLayout.findViewById(R.id.profile_image)
        val loginM = headerLayout.findViewById<TextView>(R.id.login_method)
        loginM.text = loginMode
        val details = headerLayout.findViewById<TextView>(R.id.user_details)
        details.text = loginDetails
        Picasso.get()
            .load(photoUrl)
            .into(photo)

        appBarConfiguration = AppBarConfiguration.Builder()
            .setDrawerLayout(drawer)
            .build()
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(toolbar!!, navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.homeFragment) {
                fab.visibility = View.VISIBLE
                supportActionBar?.displayOptions =
                    ActionBar.DISPLAY_SHOW_HOME or ActionBar.DISPLAY_SHOW_TITLE
                supportActionBar?.setDisplayShowHomeEnabled(true)
                supportActionBar?.setDisplayShowTitleEnabled(true)
            } else {
                supportActionBar?.displayOptions =
                    ActionBar.DISPLAY_SHOW_TITLE or ActionBar.DISPLAY_HOME_AS_UP
                supportActionBar?.setHomeButtonEnabled(true)
                supportActionBar?.setDisplayShowTitleEnabled(true)
                fab.visibility = View.GONE
            }
        }
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> {

                }
            }
            drawer.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }

    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}