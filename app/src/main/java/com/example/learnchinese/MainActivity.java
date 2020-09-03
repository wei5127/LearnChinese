package com.example.learnchinese;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText account, password;
    TextView loginStatus;
    Button loginButton,createAccount;

    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        account = (EditText) findViewById(R.id.account);
        password = (EditText) findViewById(R.id.password);
        loginStatus = (TextView) findViewById(R.id.loginStatus);
        loginButton = (Button) findViewById(R.id.loginButton);
        createAccount = (Button)findViewById(R.id.createAcctount);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mAuth = FirebaseAuth.getInstance();

        updateUserStatus();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                updateUserStatus();
            }
        });

    }
    public void login(View v){
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            String accountString = account.getText().toString();
            String passworString = password.getText().toString();
            if(accountString.isEmpty()){
                Toast.makeText(this,"Please Enter Account",Toast.LENGTH_LONG).show();
                return;
            }
            if(passworString.isEmpty()){
                Toast.makeText(this,"Please Enter Password",Toast.LENGTH_LONG).show();
                return;
            }
            mAuth.signInWithEmailAndPassword(accountString,passworString)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this,"Login Fail:"+e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                        }
                    });

        }else {
            mAuth.signOut();
        }
    }

    public void updateUserStatus(){
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            loginButton.setText("LogIn");
            loginStatus.setText("未登入");
            createAccount.setVisibility(View.VISIBLE);
        }else {
            loginButton.setText("LogOut");
            loginStatus.setText("己登入\n"+user.getEmail()+"\nNickName:"+user.getDisplayName());
            createAccount.setVisibility(View.INVISIBLE);
        }

    }


    public void createNewAccount(View v) {
        Intent intent = new Intent(this, NewAccountActivity.class);
        startActivity(intent);
    }

}