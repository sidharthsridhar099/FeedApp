package com.feedapp.feedapp.Views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feedapp.feedapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static String TAG = "LoginActivity";
    private LinearLayout mRegisterLinearLay,mLoginLinrLayout;
    private TextView mEmailInput,mPassword,mNewUserRegister,mRegisterEmail,mRegisterPhone,mRegisterPassword,mRegisterConfrmPass,mLoginText;
    private Button mLoginBtn,mRegisterBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mInItObjects();
        mInItWidgets();
    }


    private void mInItObjects() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    private void mInItWidgets() {
        mRegisterLinearLay =    (LinearLayout) findViewById(R.id.registration_box);
        mLoginLinrLayout =      (LinearLayout) findViewById(R.id.login_box);
        mEmailInput      =      (TextView) findViewById(R.id.xLogin_email_input);
        mPassword        =      (TextView) findViewById(R.id.xLogin_password_input);
        mNewUserRegister =      (TextView) findViewById(R.id.new_registration_request);
        mLoginBtn        =      (Button) findViewById(R.id.xLogin_button);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()){
                    Log.e(TAG,"VALIDATION SUCCESS");
                    String email = mEmailInput.getText().toString();
                    String password = mPassword.getText().toString();
                    signInWithExistingUser(email,password);
                }
            }
        });

        mNewUserRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginLinrLayout.setVisibility(View.GONE);
                mRegisterLinearLay.setVisibility(View.VISIBLE);
                initializeRegistrationScreen();
            }
        });

    }

    private void initializeRegistrationScreen() {
        mRegisterEmail = findViewById(R.id.registration_email_input);
        mRegisterPhone  = findViewById(R.id.registration_phone_number_input);
        mRegisterPassword    = findViewById(R.id.registration_password_input_1);
        mRegisterConfrmPass    = findViewById(R.id.registration_password_input_2);
        mRegisterBtn                = findViewById(R.id.register_button);
        mLoginText                     = findViewById(R.id.login_request);

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateRegisterForm()) {
                    String email = mRegisterEmail.getText().toString();
                    String password = mRegisterPassword.getText().toString();
                    createUserWithEmailPassword(email,password);
                }
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRegisterLinearLay.setVisibility(View.GONE);
                mLoginLinrLayout.setVisibility(View.VISIBLE);
            }
        });

        mLoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRegisterLinearLay.setVisibility(View.GONE);
                mLoginLinrLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void createUserWithEmailPassword(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(LoginActivity.this, "Registered Successfully",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());

                           // updateUI(null);
                        }

                        // ...
                    }
                });

    }


    private void signInWithExistingUser(String email, String password){
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
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                           // updateUI(null);
                        }

                        // ...
                    }
                });
    }


    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("userId",user.getUid());
        startActivity(intent);
        finish();
    }


    //Field Validation
    public boolean validateForm()
    {
        boolean a,b;
        a = !mEmailInput.getText().toString().isEmpty();
        b = !mPassword.getText().toString().isEmpty();

        if(!a)
        {
            mEmailInput.setError("Email cannot be empty");
        }
        else
        {
            mEmailInput.setError(null);
        }
        if(!b)
        {
            mPassword.setError("Password cannot be empty");
        }
        else
        {
            mPassword.setError(null);
        }
        Log.d(TAG,"email validation : " + a);
        Log.d(TAG,"password validation : " + b);
        Log.d(TAG,"return value : " + (a && b));
        return a && b;
    }


    //Field Validation
    public boolean validateRegisterForm()
    {
        boolean c,d,e,f;
        c = !mRegisterPhone.getText().toString().isEmpty();
        d = !mRegisterEmail.getText().toString().isEmpty();
        e = !mRegisterPassword.getText().toString().isEmpty();
        f = !mRegisterConfrmPass.getText().toString().isEmpty();

        if(!c)
        {
            mRegisterPhone.setError("Number cannot be empty");
        }
        else
        {
            mRegisterPhone.setError(null);
        }
        if(!d)
        {
            mRegisterEmail.setError("Email cannot be empty");
        }
        else
        {
            mRegisterEmail.setError(null);
        }
        if(!e)
        {
            mRegisterPassword.setError("Password cannot be empty");
        }
        else
        {
            mRegisterPassword.setError(null);
        }
        if(!f)
        {
            mRegisterConfrmPass.setError("Password cannot be empty");
        }
        else
        {
            mRegisterConfrmPass.setError(null);
        }

        return c && d && e && f;
    }



}
