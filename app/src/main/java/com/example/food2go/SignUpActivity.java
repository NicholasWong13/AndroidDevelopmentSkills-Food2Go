package com.example.food2go;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;

public class SignUpActivity extends AppCompatActivity {

    EditText temail, tpassword, tuserName, tAddress, tPhoneNumber;
    TextView btn, btn2;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference mDatabase;
    TextView AdminLink, NotAdminLink;
    private static final String USER = "user";
    private static final String ADMIN = "admin";
    private static final String TAG = "SignUpActivity";
    private User user;
    private Admin admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        temail = (EditText) findViewById(R.id.editTextTextPersonName5);
        tpassword = (EditText) findViewById(R.id.editTextTextPersonName2);
        btn = (TextView) findViewById(R.id.button1);
        btn2 = (TextView) findViewById(R.id.button2);
        tuserName = findViewById(R.id.editTextTextPersonName6);
        tPhoneNumber = findViewById(R.id.phoneNumberProfileTxt);
        tAddress = findViewById(R.id.AddressProfileTxt);


        database = FirebaseDatabase.getInstance();

        mDatabase = database.getReference(USER);


        mAuth = FirebaseAuth.getInstance();


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = temail.getText().toString();
                String password = tpassword.getText().toString();
                String userName = tuserName.getText().toString();
                String address = tAddress.getText().toString();
                String phoneNumber = tPhoneNumber.getText().toString();


                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(userName) || TextUtils.isEmpty(address) || TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(SignUpActivity.this, "Fill all fields.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    user = new User(email, password, userName, phoneNumber, address);
                                    FirebaseDatabase.getInstance().getReference("user")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(SignUpActivity.this, "User has been registred succesfully!", Toast.LENGTH_LONG).show();
                                                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                                        startActivity(intent);
                                                    } else {
                                                        Toast.makeText(SignUpActivity.this, "ERROR!", Toast.LENGTH_LONG).show();

                                                    }

                                                }
                                            });

                                } else {
                                    Toast.makeText(SignUpActivity.this, "ERROR!", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });


        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
        }
    }
}