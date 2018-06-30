package com.jim.quickjournal.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.jim.quickjournal.R;
import java.util.Arrays;
import java.util.List;

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
      naVigateTomain();
      finish();
    }else {
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
  private  void naVigateTomain(){
    Intent intent= new Intent(LoginActivity.this, MainActivity.class);
    startActivity(intent);
  }




}
