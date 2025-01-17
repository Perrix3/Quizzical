package com.example.quizzical;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    private  FirebaseAuth mAuth;
    private EditText mail,pwd,conf;
    private Button signup;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth= FirebaseAuth.getInstance();

        mail=findViewById(R.id.editMail);
        pwd=findViewById(R.id.editPwd);
        conf=findViewById(R.id.editConfPwd);
        signup=findViewById(R.id.signUpButton);

        //Sends to sign up method
        signup.setOnClickListener(view ->{
            signUp(mail.getText().toString(),pwd.getText().toString(),conf.getText().toString());
        });


    }

    //Signs up
    private void signUp(String mail, String pwd, String confPwd){
        if(TextUtils.isEmpty(mail)||TextUtils.isEmpty(pwd)||TextUtils.isEmpty(confPwd)){
            Toast.makeText(this, "You must fill all the fields.", Toast.LENGTH_SHORT).show();
            Log.w("Signup", "Not all fields are filled.");
            return;
        }else if(!pwd.equals(confPwd)){
            Toast.makeText(this, "Passwords don't match.", Toast.LENGTH_SHORT).show();
            Log.w("Signup", "Passwords don't match.");
            return;
        } else{
            mAuth.createUserWithEmailAndPassword(mail,pwd).addOnCompleteListener(task -> {
               if(task.isSuccessful()){
                   Log.d("Signup","User created succesfully.");
                       Intent intent=new Intent(SignupActivity.this, MainMenuActivity.class);
                       startActivity(intent);
               } else{
                   Log.w("Signup","SignUp failed, ", task.getException());
               }
            });
        }
    }
}
