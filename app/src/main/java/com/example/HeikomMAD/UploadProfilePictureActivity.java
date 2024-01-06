package com.example.HeikomMAD;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class UploadProfilePictureActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private ImageView imageViewUploadPic;
    private FirebaseAuth authProfile;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;
    private static final int PICK_IMAGE_REQUEST=1;
    private Uri uriImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_picture);


        authProfile=FirebaseAuth.getInstance();
        Button buttonUploadPicChoose=findViewById(R.id.upload_pic_choose_button);
        Button buttonUploadPic=findViewById(R.id.upload_pic_button);
        progressBar=findViewById(R.id.progressBar);
        imageViewUploadPic=findViewById(R.id.imageVIew_profile_dp);
        firebaseUser=authProfile.getCurrentUser();
        storageReference= FirebaseStorage.getInstance("gs://heikommadapp.appspot.com").getReference("Display pics");
        Uri uri=firebaseUser.getPhotoUrl();
        //Set User's current Dp in Imageview (If uploaded already)
        Picasso.with(UploadProfilePictureActivity.this).load(uri).into(imageViewUploadPic);

        //Choosing image to Upload
        buttonUploadPicChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();

            }
        });
        //Upload Image
        buttonUploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                UploadPic();
            }
        });

    }
    private void UploadPic(){
        if (uriImage!=null){
            //Svae image with uid of the currently logged user
            StorageReference fileReference=storageReference.child(authProfile.getCurrentUser().getUid()+"."+getFileExtension(uriImage)) ;
            //Upload Image to Storage
            fileReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUri=uri;
                            firebaseUser=authProfile.getCurrentUser();
                            //Finally set the display image of the user after upload
                            UserProfileChangeRequest profileUpdates=new UserProfileChangeRequest.Builder().setPhotoUri(downloadUri).build() ;
                            firebaseUser.updateProfile(profileUpdates);
                            Picasso.with(UploadProfilePictureActivity.this).invalidate(uri);
                            // Load the new image into the ImageView
                            Picasso.with(UploadProfilePictureActivity.this).load(uri).into(imageViewUploadPic);
                        }
                    });
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadProfilePictureActivity.this,"Upload Successful!",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(UploadProfilePictureActivity.this,UserProfileActivity.class);
                    startActivity(intent);
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadProfilePictureActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        } else{
            Toast.makeText(UploadProfilePictureActivity.this,"No File Selected",Toast.LENGTH_SHORT).show();
        }
    }
    //Obtain File Extension of Image
    private String getFileExtension(Uri uri){
        ContentResolver cR=getContentResolver() ;
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void openFileChooser(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT)   ;
        startActivityForResult(intent,PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null)    {
            uriImage=data.getData();
            imageViewUploadPic.setImageURI(uriImage);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.common_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    //When any menu item is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.menu_refresh){
            //refresh Activity
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        } else if (id==R.id.menu_update_profile) {
            Intent intent = new Intent(UploadProfilePictureActivity.this,UpdateProfileActivity.class);
            startActivity(intent);
            finish();
        }


        return super.onOptionsItemSelected(item);
    }
}