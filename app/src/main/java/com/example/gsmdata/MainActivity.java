package com.example.gsmdata;

import android.content.Intent;
import android.os.Bundle;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class MainActivity  extends BaseActivity implements
        View.OnClickListener  {
    private  static final  String TAG ="MainActivity";
    private FirebaseAuth mAuth;

    private EditText mEmail,mPassword;
    private Button logIn;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_first);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setProgressBar(R.id.progressBar);

        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();




        mEmail= findViewById(R.id.email);
        mPassword= findViewById(R.id.editText4);
        findViewById(R.id.login).setOnClickListener( this);




       


    }



    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressBar();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();


                            updateUI(user);


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);



                }
                hideProgressBar();

            }
        });
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }
    private boolean validateForm() {
        boolean valid = true;

        String email = mEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmail.setError("Required.");
            valid = false;
        } else {
            mEmail.setError(null);
        }

        String password = mPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPassword.setError("Required.");
            valid = false;
        } else {
            mPassword.setError(null);
        }

        return valid;
    }
    private void updateUI(FirebaseUser user) {
        hideProgressBar();
        if (user != null) {
            Toast.makeText(MainActivity.this, "Authentication success.",
                    Toast.LENGTH_SHORT).show();
            Intent intent =new Intent(MainActivity.this, FirstFragment.class);
            startActivity(intent);

        }
    }


    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.login) {
            signIn(mEmail.getText().toString(), mPassword.getText().toString());

        }
}
}
