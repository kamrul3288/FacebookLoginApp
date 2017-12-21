package com.kamrulhasan3288.facebookloginapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.login.LoginManager;

public class AccountActivity extends AppCompatActivity {

    private TextView accountId,phoneNumberText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        accountId = findViewById(R.id.profileId);
        phoneNumberText = findViewById(R.id.profilePhoneNumber);

        if (getSupportActionBar()!= null){
            getSupportActionBar().setTitle("Account");
        }

        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(Account account) {

                accountId.setText("Account ID : "+account.getId());

                if (account.getPhoneNumber()!= null){
                    phoneNumberText.setText("Phone Number : "+String.valueOf(account.getPhoneNumber()));
                }else {
                    phoneNumberText.setText("Email : "+String.valueOf(account.getEmail()));
                }

            }

            @Override
            public void onError(AccountKitError accountKitError) {

            }
        });
    }

    public void onLogout(View view) {
        AccountKit.logOut();
        LoginManager.getInstance().logOut();
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }
}
