package com.example.learnchinese;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class NewAccountActivity extends AppCompatActivity {

    EditText newAccount,newNickName,pw1,pw2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);
        newAccount = (EditText)findViewById(R.id.newAccount);
        newNickName = (EditText)findViewById(R.id.newNickName);
        pw1 = (EditText)findViewById(R.id.newPW1);
        pw2 = (EditText)findViewById(R.id.newPW2);
    }


    public void createNewAccount(View v){
        final String account = newAccount.getText().toString();
        final String nickname = newNickName.getText().toString();
        final String password = pw1.getText().toString();
        String cPassword = pw2.getText().toString();

        if(account.isEmpty()){
            Toast.makeText(this,"Please Enter Account",Toast.LENGTH_LONG).show();
            return;
        }

        if(nickname.isEmpty()){
            Toast.makeText(this,"Please Enter NickName",Toast.LENGTH_LONG).show();
            return;
        }

        if(!(password.equals(cPassword))){
            Toast.makeText(this,"Confirm Password",Toast.LENGTH_LONG).show();
            return;
        }

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(account,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(NewAccountActivity.this,"Account Created!",Toast.LENGTH_LONG).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                            .setDisplayName(nickname).build();
                    user.updateProfile(request);
                    mAuth.signOut();
                    mAuth.signInWithEmailAndPassword(account,password);

                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NewAccountActivity.this,"Fail:"+e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}

