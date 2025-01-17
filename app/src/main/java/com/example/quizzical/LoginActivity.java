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
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private EditText mail,pwd;
    private Button login, signup;
    private ImageButton github, google;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN=3600;

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
            Log.w("Mail Login", "All fields must be filled.");
            Toast.makeText(this, "Please fill out all the fields.", Toast.LENGTH_SHORT).show();
            return;
        } else{
            mAuth.signInWithEmailAndPassword(mail,pwd).addOnCompleteListener(task ->{
                if (task.isSuccessful()) { //If correct, send a toast and send to next view
                    FirebaseUser user=mAuth.getCurrentUser();
                    Toast.makeText(LoginActivity.this, "Login successful. Welcome " + user.getEmail(), Toast.LENGTH_SHORT).show();
                    Log.d("Mail Login", "Login successful.",task.getException());
                    Intent intent=new Intent(LoginActivity.this, MainMenuActivity.class);
                    startActivity(intent);
                } else { //If not, give error message
                    Log.w("Mail Login", "Authentication failed: ",task.getException());
                    Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
            });
        }
    }


    //Google login start
    private void googleLogin(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Use the web client ID from strings.xml
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);

        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Success :)
                Log.d("GoogleSignIn", "Google sign-in successful.");
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w("GoogleSignIn", "Google sign-in failed.", e);
                Toast.makeText(this, "Google Sign-In failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Authentication with google id
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        //Success :)
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(this, "Welcome " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                        Log.d("GoogleAuth", "Succesful login with Google.", task.getException());
                        Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                        startActivity(intent);
                    } else {
                        //Fail :(
                        Log.w("GoogleAuth", "Firebase authentication failed", task.getException());
                        Toast.makeText(this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //Github Login
    private void githubLogin(){
        OAuthProvider.Builder pr=OAuthProvider.newBuilder("github.com");
        Task<AuthResult> t=FirebaseAuth.getInstance().startActivityForSignInWithProvider(this,pr.build());
        t.addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                //Successful :)
                FirebaseUser u=FirebaseAuth.getInstance().getCurrentUser();
                Log.d("GithubLogin","User: "+u.getDisplayName());
                Intent intent=new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            } else{
                //Failed >:(
                Log.w("GithubLogin","An error occured while signing in with GitHub", task.getException());
            }
        });
    }


}
