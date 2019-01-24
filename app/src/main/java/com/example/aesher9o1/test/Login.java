package com.example.aesher9o1.test;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;

public class Login extends AppCompatActivity {

    private CallbackManager callbackManager;
    private LoginButton facebookLogin;
    private String TAG = "LoginActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();



        setContentView(R.layout.activity_login);


        callbackManager = CallbackManager.Factory.create();
        facebookLogin = findViewById(R.id.login_button);


        facebookLogin.setReadPermissions(Collections.singletonList("email"));


        if(isLoggedIn)
            sendToMain(false);
        else{
            facebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    sendToMain(true);
                }

                @Override
                public void onCancel() {
                    Log.w(TAG, "cancelled");
                }

                @Override
                public void onError(FacebookException exception) {
                    Log.w(TAG, exception.toString());
                }
            });
        }

    }

    private void sendToMain(boolean isLogin){

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("loginStatus",isLogin);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
