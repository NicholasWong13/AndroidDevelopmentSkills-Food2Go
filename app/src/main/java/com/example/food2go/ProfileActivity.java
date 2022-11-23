package com.example.food2go;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseUser user;
    private String userId;
    private DatabaseReference reference;
    private CircleImageView profileImageView;
    private AppCompatButton closeBtn,saveBtn,profileChangeBtn;
    private ImageView logoutBtn,backBtn,CardBtn;
    private FirebaseAuth mAuth;

    private Uri imageUri;
    private String myUri ="";
    private StorageTask uploadTask;
    private StorageReference storageProfilePicsRef;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logoutBtn =(ImageView) findViewById(R.id.logoutBtn);
        user  = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("user");
        userId=user.getUid();
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("ProfilePic");
        profileImageView = findViewById(R.id.dp);

        profileChangeBtn = findViewById(R.id.changeProfilePic);
        mAuth = FirebaseAuth.getInstance();
        closeBtn = findViewById(R.id.cancelBtn);
        saveBtn = findViewById(R.id.saveBtn);
        backBtn = findViewById(R.id.backBtn);
        CardBtn = findViewById(R.id.CardBtn);

        CardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,CardListActivity.class);
                startActivity(intent);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,MainActivity.class));
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProfileImage();
            }
        });

        profileChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio(1,1).start(ProfileActivity.this);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(ProfileActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        getUserInfo();




        final TextView Emailtxt= (TextView) findViewById(R.id.EmailTxt);
        final TextView AddressTxt= (TextView) findViewById(R.id.AddressTxt);

        final TextView MobileTxt = (TextView) findViewById(R.id.MobileTxt);
        final TextView UserIdTxt = (TextView) findViewById(R.id.UserIdTxt);
        final TextView UserNameTxt = (TextView) findViewById(R.id.UserNameTxt);

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);




                if(userProfile !=null){
                    String Email = userProfile.email;
                    String Address = userProfile.address;
                    String Mobile = userProfile.phoneNumber;
                    String UserName = userProfile.userName;


                    UserNameTxt.setText(UserName);
                    UserIdTxt.setText(userId);
                    Emailtxt.setText(Email);
                    AddressTxt.setText(Address);
                    MobileTxt.setText(Mobile);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this,"Something went wrong" , Toast.LENGTH_LONG).show();
            }
        });



    }

    private void getUserInfo() {
        reference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount() > 0){




                    if(snapshot.hasChild("image"))
                    {
                        String image = snapshot.child("image").getValue().toString();
                        Picasso.get().load(image).into(profileImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data!=null && resultCode == RESULT_OK)  {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            profileImageView.setImageURI(imageUri);
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            Toast.makeText(this,"Error , Try Again",Toast.LENGTH_SHORT).show();

        }
    }


    private void uploadProfileImage() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Set Your Profile");
        progressDialog.setMessage("Please wait, while we are setting your data");
        progressDialog.show();

        if(imageUri != null){
            final StorageReference fileRef = storageProfilePicsRef
                    .child(userId + ".jpg");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return  fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUrl=task.getResult();
                        myUri = downloadUrl.toString();

                        HashMap<String,Object> userMap = new HashMap<>();
                        userMap.put("image",myUri);
                        reference.child(userId).updateChildren(userMap);
                        progressDialog.dismiss();
                    }
                }
            });
        }
        else{
            Toast.makeText(this,"Image not selected",Toast.LENGTH_SHORT).show();
        }

    }
}