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
package com.jim.quickjournal.ui.views

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
//import com.firebase.ui.auth.AuthUI
//import com.firebase.ui.auth.AuthUI.IdpConfig.*
//import com.google.firebase.auth.FirebaseAuth
import com.jim.quickjournal.R
import com.jim.quickjournal.databinding.ActivityLoginBinding

/**
 * The Main launcher
 * activity prompts users to log in if not logged in
 *
 */
class LoginFragment : Fragment() {
    // Choose authentication providers
/*    private var providers = listOf(
        EmailBuilder().build(),
        PhoneBuilder().build(),
        GoogleBuilder().build()
    )*/

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    internal var view: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityLoginBinding.inflate(inflater, container, false)
        view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.view = view
        //Check if User is already singed in
      /*  val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            //user is already signed in
            navToMain()
        } else {
            //prompt th user to login
            signIn()
        }*/
    }

    private fun signIn() {
      /*  val intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.drawable.journal) // Set logo drawable
            .setTheme(R.style.AppTheme) // Set theme
            .build()
        startForResult.launch(intent)*/
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if ((result.resultCode == Activity.RESULT_OK) && (result.data != null)) {
                // IdpResponse response = IdpResponse.fromResultIntent(data);
                // Successfully signed in
                navToMain()
            }
        }


    //Launch the Main App
    private fun navToMain() {
        this.findNavController().navigate(R.id.action_nav_to_homeFragment)
    }

    companion object {
        private const val RC_SIGN_IN = 100
    }
}