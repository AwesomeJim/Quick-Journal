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
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

import com.jim.quickjournal.R
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration


//    var mAuth: FirebaseAuth? = null
//    var user: FirebaseUser? = null
    var toolbar: Toolbar? = null
    var loginMode: String? = null
    var loginDetails: String? = null
    var photo: CircleImageView? = null
    var photoUrl: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        navController = findNavController(R.id.nav_host_fragment)
//        //Initializes the Firebase instance
//        mAuth = FirebaseAuth.getInstance()
//
//        //check if the user is login
//        user = mAuth!!.currentUser
//        if (user != null) {
            //User is logged in get their details and initialize the views and Load Journals
           // loadGoogleUserDetails()
            initViews()
        //} else {
            //User Not Logged In
            //navController.navigate(R.id.action_nav_to_loginFragment)
        //}
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
        *//**
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
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val headerLayout = navigationView.getHeaderView(0)
        photo = headerLayout.findViewById(R.id.profile_image)
        val loginM = headerLayout.findViewById<TextView>(R.id.login_method)
        loginM.text = loginMode
        val details = headerLayout.findViewById<TextView>(R.id.user_details)
        details.text = loginDetails
        Picasso.get()
            .load(photoUrl)
            .into(photo)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_profile, R.id.nav_help, R.id.nav_logout
            ), drawer
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navigationView.setupWithNavController(navController)
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
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId
        if (id == R.id.nav_logout) {
           /* AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    navController.navigate(R.id.action_nav_to_loginFragment)
                }*/
        }
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }


}