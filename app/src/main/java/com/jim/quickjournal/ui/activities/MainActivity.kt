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

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.jim.quickjournal.R
import com.jim.quickjournal.adaptor.JournalAdapter
import com.jim.quickjournal.db.entity.JournalEntry
import com.jim.quickjournal.viewmodel.JournalViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    private val viewModel: JournalViewModel by viewModels()

    //Activity Member Variables
    @Inject
    lateinit var mAdapter: JournalAdapter

    var mAuth: FirebaseAuth? = null
    var user: FirebaseUser? = null
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

        //Initializes the Firebase instance
        mAuth = FirebaseAuth.getInstance()

        //check if the user is login
        user = mAuth!!.currentUser
        if (user != null) {
            //User is logged in get their details and initialize the views and Load Journals
            loadGoogleUserDetails()
            initViews()
            setUpViewModel()
        } else {
            //User Not Logged In
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Get the details of the Logged in User
     */
    private fun loadGoogleUserDetails() {
        /**
         * Check which method/Provider a user Used to login
         */
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
    }

    private fun initViews() {
        // Set the RecyclerView to its corresponding view
        val mRecyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize the adapter and attach it to the RecyclerView
        mRecyclerView.adapter = mAdapter

        //Floating Action Button for adding a Journal entry
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val startAddJournalActivity = Intent(this@MainActivity, AddJournalActivity::class.java)
            startActivity(startAddJournalActivity)
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
            AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                }
        }
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setUpViewModel() {
        viewModel.loadAllJournals()
        viewModel.journalList.observe(this) {
            mAdapter.setJournalsList(
                context = this,
                it
            )
            mAdapter.notifyDataSetChanged()
        }
        mAdapter.setOnItemClickListener { _, any2, _, ->
            val journalEntry = any2 as JournalEntry
            val intent = Intent(this@MainActivity, JournalDetailActivity::class.java)
            intent.putExtra(AddJournalActivity.EXTRA_JOURNAL_ID, journalEntry.id)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadAllJournals()
    }

}