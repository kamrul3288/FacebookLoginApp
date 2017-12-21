package com.kamrulhasan3288.facebookloginapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class LoginActivity extends AppCompatActivity {

    public static int APP_REQUEST_CODE = 99;
    private LoginButton fbLoginButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fbLoginButton = findViewById(R.id.facebook_login_button);
        fbLoginButton.setReadPermissions("email");

        //login button callback registration
        callbackManager = CallbackManager.Factory.create();
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                launchAccountActivity();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"You close this",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),"Server Error occur",Toast.LENGTH_SHORT).show();
            }
        });

        // check for an access token account kit
        AccessToken accessToken = AccountKit.getCurrentAccessToken();
        // check for an access token for facebook button
        com.facebook.AccessToken loginToken = com.facebook.AccessToken.getCurrentAccessToken();
        if (accessToken != null || loginToken != null){
            launchAccountActivity();
        }

    }


    public void loginWithPhoneNumber(View view) {
        onLogin(LoginType.PHONE);
    }

    public void loginWithEmail(View view) {
        onLogin(LoginType.EMAIL);
    }


    private void onLogin(LoginType loginType) {
        // crate intent for create the account kit activity
        final Intent intent = new Intent(LoginActivity.this, AccountKitActivity.class);

        //configure login type and response type
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(loginType, AccountKitActivity.ResponseType.TOKEN);

        final AccountKitConfiguration configuration = configurationBuilder.build();

        //launch account kit activity
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configuration);
        startActivityForResult(intent, APP_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //forward result to the callback manager for login button
        callbackManager.onActivityResult(requestCode,resultCode,data);

        // confirm that this response matches your request
        if (requestCode == APP_REQUEST_CODE) {

            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);

            if (loginResult.getError() != null) {
                // display login error
                String message = loginResult.getError().getErrorType().getMessage();
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

            } else if (loginResult.getAccessToken() != null) {
                // successful proceed to the account activity
                launchAccountActivity();
            }


        }

    }


    private void launchAccountActivity() {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
        finish();
    }


}
