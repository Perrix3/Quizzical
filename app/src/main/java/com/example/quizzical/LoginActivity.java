package com.example.quizzical;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private EditText mail,pwd;
    private Button login, signup;
    private ImageButton github, google;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Gets the editTexts
        mail=findViewById(R.id.editMail);
        pwd=findViewById(R.id.editPwd);
        //Gets buttons
        login=findViewById(R.id.loginButton);
        signup=findViewById(R.id.signUp);
        //Gets imageButtons
        github=findViewById(R.id.github);
        google=findViewById(R.id.google);

        mAuth = FirebaseAuth.getInstance();


        //Send to login method
        login.setOnClickListener(view ->  {
            String email=mail.getText().toString();
            String pass=pwd.getText().toString();
            login(email, pass);
        });

    //Send to sign up activity
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        //Send to github login method
        github.setOnClickListener(view -> {
            githubLogin();
        });

        google.setOnClickListener(view -> {
            googleLogin();
        });




    }

//Manages firebase login and authentication, need to change texts to @strings
    private void login(String mail, String pwd){
        if(TextUtils.isEmpty(mail)||TextUtils.isEmpty(pwd)){
            Toast.makeText(this, "Please fill out all the fields.", Toast.LENGTH_SHORT).show();
            return;
        } else{
            mAuth.signInWithEmailAndPassword(mail,pwd).addOnCompleteListener(task ->{
                if (task.isSuccessful()) { //If correct, send a toast and send to next view
                    FirebaseUser user=mAuth.getCurrentUser();
                    Toast.makeText(LoginActivity.this, "Login successful. Welcome " + user.getEmail(), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(LoginActivity.this, MainMenuActivity.class);
                    startActivity(intent);
                } else { //If not, give error message
                    Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
            });
        }
    }


    //Google login
    private void googleLogin(){

    }

    //Github Login
    private void githubLogin(){
        OAuthProvider.Builder pr=OAuthProvider.newBuilder("github.com");
        Task<AuthResult> t=FirebaseAuth.getInstance().startActivityForSignInWithProvider(this,pr.build());
        t.addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                //Succesful
                FirebaseUser u=FirebaseAuth.getInstance().getCurrentUser();
                Log.d("GithubLogin","User: "+u.getDisplayName());
                Intent intent=new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            } else{
                //Failed
                Log.w("GithubLogin","An error occured while signing in with GitHub", task.getException());
            }
        });
    }


}
