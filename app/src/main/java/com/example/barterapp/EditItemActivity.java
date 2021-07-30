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

public class EditItemActivity extends AppCompatActivity {
    private ImageView edititemIV;
    private EditText edititem_nameET;
    private EditText edititem_descET;
    private EditText edititem_categoryET;
    private EditText editpreferred_itemET;
    private EditText edititem_conditionET;
    private EditText edititem_quantityET;
    Button SaveBtn;
    private Uri mImageUri;
    private String imageAddress="";
    private String catagory="";
    TradeItemModelClass tradeItemModelClass=new TradeItemModelClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        getSupportActionBar();
        tradeItemModelClass = (TradeItemModelClass) getIntent().getSerializableExtra("item");
        init_view();

    }

    private void init_view() {
        edititem_nameET = findViewById(R.id.editnameitemET);
        edititem_descET = findViewById(R.id.edititemDescriptionET);
        edititem_categoryET = findViewById(R.id.editcategoryTagsET);
        editpreferred_itemET = findViewById(R.id.editpreferTrade_Items_ET);
        edititem_conditionET = findViewById(R.id.edititem_condET);
        edititem_quantityET = findViewById(R.id.edititem_quantityET);
        SaveBtn = findViewById(R.id.edit_save_btn);
        edititemIV = findViewById(R.id.editthumbnailImageView);
        edititem_nameET.setText(tradeItemModelClass.getName());
        edititem_descET.setText(tradeItemModelClass.getDescription());
        editpreferred_itemET.setText(tradeItemModelClass.getTradeWith());
        edititem_conditionET.setText(tradeItemModelClass.getCondition());
        edititem_quantityET.setText("" + tradeItemModelClass.getQuantity());
        catagory = tradeItemModelClass.getCategory();
        setCatagoryRadioButton(tradeItemModelClass.getCategory());

        Picasso.get().load(tradeItemModelClass.getImageUrl()).into(edititemIV);
        imageAddress = tradeItemModelClass.getImageUrl();

        edititemIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        SaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateUserData();
            }
        });
    }

    private void setCatagoryRadioButton(String Category) {
        String[] catagorys = new String[] {"All", "Wearable", "Electrical", "Sports",
                "Kitchen", "Home Deco", "Consumable"};
        RadioButton radioButton;
        switch (Category) {
            case "Clothes":
                radioButton=findViewById(R.id.radioButtonWearable);
                radioButton.setChecked(true);
                break;
            case "Accessories":
                radioButton=findViewById(R.id.radioButtonElectrical);
                radioButton.setChecked(true);
                break;
            case "Sports":
                radioButton=findViewById(R.id.radioButtonSports);
                radioButton.setChecked(true);
                break;
            case "Kitchen":
                radioButton=findViewById(R.id.radioButtonKitchen);
                radioButton.setChecked(true);
                break;
            case "Home Decoration":
                radioButton=findViewById(R.id.radioButtonHome_Deco);
                radioButton.setChecked(true);
                break;
            case "Consumable":
                radioButton=findViewById(R.id.radioButtonConsumable);
                radioButton.setChecked(true);
                break;

        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    // using the result of selection of image and passing the uri to show it and upload it through uploadImage() method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            uploadImage(mImageUri);
            Picasso.get().load(mImageUri).into(edititemIV);
        }

    }

    //// Input Data validation start //////////////////////////////////////
    private boolean validateUserData() {
        try {
            if (imageAddress.equals("")){
                Toast.makeText(this,"Zero image detected.",Toast.LENGTH_SHORT);
                return false;
            }
            if (edititem_nameET.getText().toString().equals("")) {
                edititem_nameET.setError("Please enter item name.");
                return false;
            }
            if (edititem_descET.getText().toString().equals("")) {
                edititem_descET.setError("Please enter item description.");
                return false;
            }
            if (catagory.toString().equals("")) {
                Toast.makeText(this,"Please select a category.",Toast.LENGTH_LONG).show();
                return false;
            }
            if (editpreferred_itemET.getText().toString().equals("")) {
                editpreferred_itemET.setError("Please enter preferred item.");
                return false;
            }
            if (edititem_conditionET.getText().toString().equals("")) {
                edititem_conditionET.setError("Please enter item condition.");
                return false;
            }
            if (edititem_quantityET.getText().toString().equals("")) {
                edititem_quantityET.setError("Please enter item quantity.");
                return false;
            }


            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            tradeItemModelClass.setTraderID(firebaseUser.getUid());
            tradeItemModelClass.setName(edititem_nameET.getText().toString());
            tradeItemModelClass.setItemId(tradeItemModelClass.getItemId());
            tradeItemModelClass.setDescription(edititem_descET.getText().toString());
            tradeItemModelClass.setCategory(catagory.toString());
            tradeItemModelClass.setTradeWith(editpreferred_itemET.getText().toString());
            tradeItemModelClass.setQuantity(Integer.parseInt(edititem_quantityET.getText().toString()));
            tradeItemModelClass.setCondition(edititem_conditionET.getText().toString());
            tradeItemModelClass.setImageUrl(imageAddress);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReference().child("trade_items");
            databaseReference.child(tradeItemModelClass.getItemId()).setValue(tradeItemModelClass);
            finish();

        }catch (Exception e){
            Toast.makeText(EditItemActivity.this," "+e.getMessage(),Toast.LENGTH_LONG).show();
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
                        // Continue with the task to get the download URL
                        return fileReference.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            try {
                                imageAddress = task.getResult().toString();

                            }catch (Exception e){
                                Toast.makeText(EditItemActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
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