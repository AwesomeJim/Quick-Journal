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

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig.*
import com.google.firebase.auth.FirebaseAuth
import com.jim.quickjournal.R

/**
 * The Main launcher
 * activity prompts users to log in if not logged in
 *
 */
class LoginActivity : AppCompatActivity() {
    // Choose authentication providers
    private var providers = listOf(
        EmailBuilder().build(),
        PhoneBuilder().build(),
        GoogleBuilder().build()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Check if User is already singed in
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            //user is already signed in
            navToMain()
            finish()
        } else {
            //prompt th user to login
            signIn()
        }
    }

    private fun signIn() {
        val intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.drawable.journal) // Set logo drawable
            .setTheme(R.style.AppTheme) // Set theme
            .build()
        startForResult.launch(intent)
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if ((result.resultCode == Activity.RESULT_OK) && (result.data != null)) {
                // IdpResponse response = IdpResponse.fromResultIntent(data);
                // Successfully signed in
                navToMain()
                finish()
            }
        }


    //Launch the Main App
    private fun navToMain() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
    }

    companion object {
        private const val RC_SIGN_IN = 100
    }
}