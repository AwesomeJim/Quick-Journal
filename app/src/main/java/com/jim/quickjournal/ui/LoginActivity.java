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

package com.jim.quickjournal.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.jim.quickjournal.R;

import java.util.Arrays;
import java.util.List;

/**
 * The Main launcher
 * activity prompts users to log in if not logged in
 *
 */
public class LoginActivity extends AppCompatActivity {

  private static final int RC_SIGN_IN = 100;

  // Choose authentication providers
  List<AuthUI.IdpConfig> providers = Arrays.asList(
      new AuthUI.IdpConfig.EmailBuilder().build(),
      new AuthUI.IdpConfig.PhoneBuilder().build(),
      new AuthUI.IdpConfig.GoogleBuilder().build());

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    //Check if User is already singed in
    FirebaseAuth auth = FirebaseAuth.getInstance();
    if (auth.getCurrentUser() != null) {
      //user is already signed in
      naVigateTomain();
      finish();
    }else {
      //prompt th user to login
      signIn();
    }

  }
public void signIn(){
  startActivityForResult(
      AuthUI.getInstance()
          .createSignInIntentBuilder()
          .setAvailableProviders(providers)
          .setLogo(R.drawable.journal) // Set logo drawable
          .setTheme(R.style.AppTheme) // Set theme
          .build(),
      RC_SIGN_IN);

}

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == RC_SIGN_IN) {
     // IdpResponse response = IdpResponse.fromResultIntent(data);
      if (resultCode == RESULT_OK) {
        // Successfully signed in
        naVigateTomain();
        finish();
      }
    }
  }

  //Launch the Main App
  private  void naVigateTomain(){
    Intent intent= new Intent(LoginActivity.this, MainActivity.class);
    startActivity(intent);
  }




}
