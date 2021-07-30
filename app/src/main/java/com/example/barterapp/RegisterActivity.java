package com.example.barterapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barterapp.model.UserModelClass;
import com.example.barterapp.modules.LoadSpinner;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG ="test" ;
    private Button regBtn;
    private EditText fullnameReg;
    private EditText emailReg;
    private EditText phoneReg;
    private EditText passwordReg;
    private EditText confirmPasswordReg;
    private EditText addressReg;
    private TextView backtologin;
    private LoadSpinner spinner;
    private ImageView userImageReg;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private Uri mImageUri;
    private String imageAddress="";
    private UserModelClass newUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        user= FirebaseAuth.getInstance().getCurrentUser();
        firebaseAuth = FirebaseAuth.getInstance();
        init();
    }

    private void init(){
        userImageReg = findViewById(R.id.reg_userImage);
        regBtn = findViewById(R.id.reg_btn);
        fullnameReg = findViewById(R.id.reg_username);
        emailReg = findViewById(R.id.reg_email);
        phoneReg = findViewById(R.id.reg_phoneNumber);
        passwordReg = findViewById(R.id.reg_password);
        confirmPasswordReg = findViewById(R.id.reg_conf_password);
        addressReg = findViewById(R.id.reg_address);
        spinner = new LoadSpinner(this);
        backtologin = findViewById(R.id.reg_login_tv);

        backtologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( RegisterActivity.this,  LoginActivity.class));
                finish();
            }
        });

        userImageReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( RegisterActivity.this,"clicked",Toast.LENGTH_SHORT).show();
                UserModelClass user=new UserModelClass();
                String name = fullnameReg.getText().toString();
                String email = emailReg.getText().toString();
                String phoneNo = phoneReg.getText().toString();
                String pass = passwordReg.getText().toString();
                String confirmPass = confirmPasswordReg.getText().toString();
                String address = addressReg.getText().toString();

                user.setUserEmail(email);
                user.setUserName(name);
                user.setUserPhoneNo(phoneNo);
                user.setUserAddress(address);


                if (imageAddress.equals("")){
                    user.setUserImage("https://firebasestorage.googleapis.com/v0/b/barter-abfe9.appspot.com/o/users%2FprofileImage.png?alt=media&token=0ad75f5e-d2f7-473f-bd1a-92460802b1c1");
                    imageAddress="https://firebasestorage.googleapis.com/v0/b/barter-abfe9.appspot.com/o/users%2FprofileImage.png?alt=media&token=0ad75f5e-d2f7-473f-bd1a-92460802b1c1";
                }else {

                    user.setUserImage(imageAddress);
                }

                if (name.isEmpty()){
                    fullnameReg.setError("Fullname is empty.");
                    return;
                }

                if (email.isEmpty()){
                    emailReg.setError("Email is empty.");
                    return;
                }
                if (phoneNo.isEmpty()){
                    phoneReg.setError("Phone Number is empty.");
                    return;
                }
                if (pass.isEmpty()){
                    passwordReg.setError("Password is empty.");
                    return;
                }

                if (confirmPass.isEmpty()  || !pass.equals(confirmPass)){
                    confirmPasswordReg.setError("Please enter same password.");
                    return;
                }

                if (address.isEmpty()){
                    addressReg.setError("Address is empty.");
                    return;
                }

                createAccount(user,pass);

            }
        });
    }

    private void createAccount(UserModelClass userModel, String password){
        spinner.startLoadingAnimation();

        firebaseAuth.createUserWithEmailAndPassword(userModel.getUserEmail(), password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){

                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            assert user != null;
                            userModel.setUserId(user.getUid());

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(userModel.getUserName())
                                    .setPhotoUri(Uri.parse(imageAddress))
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                FirebaseDatabase fdb= FirebaseDatabase.getInstance();
                                                DatabaseReference databaseReference=fdb.getReference("users");
                                                databaseReference.child(userModel.getUserId()).setValue(userModel);



                                                Log.d("testing", "User profile updated.");
                                                Intent intent=new Intent( RegisterActivity.this, DashboardActivity.class);
                                                startActivity(intent);

                                            }
                                        }
                                    });

                        }else {

                            spinner.stopLoadingAnimation();
                            Toast.makeText( RegisterActivity.this, "Invalid: "+
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }



    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            uploadImage(mImageUri);
            Picasso.get().load(mImageUri).into(userImageReg);

        }

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadImage(Uri uri) {
        Log.d(TAG, "uploadImage: ");
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("users");
        final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                + "." + getFileExtension(mImageUri));

        final UploadTask uploadTask = fileReference.putFile(mImageUri);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();

                        }
                        return fileReference.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            try {
                                imageAddress = task.getResult().toString();
                                Log.d(TAG, "uploadImage: "+task.getResult().toString());
                            }catch (Exception e){
                                Toast.makeText(RegisterActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "uploadImage: "+e.getMessage());
            }
        });
    }

    public void deleteByUrl(String imageUrl){
        StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("delete", "onSuccess: Successfully deleted.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("delete", "onFailure: Fail to delete the file.");
            }
        });
    }

}