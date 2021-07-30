package com.example.barterapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.barterapp.model.TradeItemModelClass;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class NewItemActivity extends AppCompatActivity {
    private ImageView itemImageView;
    private EditText item_nameET;
    private EditText item_desET;
    private EditText item_categoryET;
    private EditText item_preferredET;
    private EditText item_conditionET;
    private EditText item_quantityET;
    Button addItembtn;
    private Uri mImageUri;
    private String imageAddress="";
    private String catagory="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        getSupportActionBar().hide();
        init_view();
    }

    private void init_view() {
        item_nameET=findViewById(R.id.nameitemET);
        item_desET=findViewById(R.id.itemDescriptionET);
        item_categoryET=findViewById(R.id.categoryTagsET);
        item_preferredET=findViewById(R.id.preferTrade_Items_ET);
        item_conditionET=findViewById(R.id.item_condET);
        item_quantityET=findViewById(R.id.item_quantityET);
        addItembtn=findViewById(R.id.addItem_addBtn);
        itemImageView=findViewById(R.id.thumbnailImageView);
        itemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        addItembtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateUserData();
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
            Picasso.get().load(mImageUri).into(itemImageView);
        }

    }

    private boolean validateUserData() {
        try {
            if (imageAddress.equals("")){
                Toast.makeText(this,"Zero image detected.",Toast.LENGTH_SHORT);
                return false;
            }
            if (item_nameET.getText().toString().equals("")) {
                item_nameET.setError("Please enter item name.");
                return false;
            }
            if (item_desET.getText().toString().equals("")) {
                item_desET.setError("Please enter item description.");
                return false;
            }
            if (catagory.toString().equals("")) {
                Toast.makeText(this,"Please select a category.",Toast.LENGTH_LONG).show();
                return false;
            }
            if (item_preferredET.getText().toString().equals("")) {
                item_preferredET.setError("Please enter preferred item.");
                return false;
            }
            if (item_conditionET.getText().toString().equals("")) {
                item_conditionET.setError("Please enter item condition.");
                return false;
            }
            if (item_quantityET.getText().toString().equals("")) {
                item_quantityET.setError("Please enter item quantity.");
                return false;
            }

            TradeItemModelClass tradeItemModelClass =new TradeItemModelClass();
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            tradeItemModelClass.setTraderID(firebaseUser.getUid());
            tradeItemModelClass.setName(item_nameET.getText().toString());
            tradeItemModelClass.setItemId("" + System.currentTimeMillis());
            tradeItemModelClass.setDescription(item_desET.getText().toString());
            tradeItemModelClass.setCategory(catagory.toString());
            tradeItemModelClass.setTradeWith(item_preferredET.getText().toString());
            tradeItemModelClass.setQuantity(Integer.parseInt(item_quantityET.getText().toString()));
            tradeItemModelClass.setCondition(item_conditionET.getText().toString());
            tradeItemModelClass.setImageUrl(imageAddress);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReference().child("trade_items");
            databaseReference.child(tradeItemModelClass.getItemId()).setValue(tradeItemModelClass);
            finish();

        }catch (Exception e){
            Toast.makeText(NewItemActivity.this," "+e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return true;
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));

    }

    private void uploadImage(Uri uri) {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("trade_items");
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

                            }catch (Exception e){
                                Toast.makeText(NewItemActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        RadioGroup group=findViewById(R.id.radio_group1);
        RadioGroup group2=findViewById(R.id.radio_group2);
        boolean checked = ((RadioButton) view).isChecked();
        String[] catagorys = new String[] {"All", "Wearable", "Electrical", "Sports",
                "Kitchen", "Home Deco", "Consumable"};
        group.clearCheck();
        group2.clearCheck();
        ((RadioButton) view).setChecked(true);
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButtonWearable:
                if (checked)
                    catagory=catagorys[1];

                break;
            case R.id.radioButtonElectrical:
                if (checked)
                    catagory=catagorys[2];
                break;

            case R.id.radioButtonSports:
                if (checked)
                    catagory=catagorys[3];

                break;
            case R.id.radioButtonKitchen:
                if (checked)
                    catagory=catagorys[4];
                break;
            case R.id.radioButtonHome_Deco:
                if (checked)
                    catagory=catagorys[5];

                break;
            case R.id.radioButtonConsumable:
                if (checked)
                    catagory=catagorys[6];
                break;
        }
    }

}