package com.jim.quickjournal;

import android.content.Intent;
import android.support.annotation.MainThread;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
      return;
    }else {
      signIn();
    }

  }
public void signIn(){
  startActivityForResult(
      AuthUI.getInstance()
          .createSignInIntentBuilder()
          .setAvailableProviders(providers)
          .setLogo(R.drawable.journal)      // Set logo drawable
          .setTheme(R.style.AppTheme)      // Set theme
          .build(),
      RC_SIGN_IN);

}

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == RC_SIGN_IN) {
      IdpResponse response = IdpResponse.fromResultIntent(data);

      if (resultCode == RESULT_OK) {
        // Successfully signed in
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        naVigateTomain();
        // ...
      } else {
        // Sign in failed. If response is null the user canceled the
        // sign-in flow using the back button. Otherwise check
        // response.getError().getErrorCode() and handle the error.
        // ...
      }
    }
  }
  private  void naVigateTomain(){
    Intent intent= new Intent(LoginActivity.this, MainActivity.class);
    startActivity(intent);
  }




}
