package com.example.HeikomMAD;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CreatePetition extends Fragment {

    private final int PICK_IMAGE_REQUEST = 1;

    private String userID = " ";
    private String name = " ";

    private User currentUser;
    private ImageView imagePlaceholder;
    private ImageView profilePic;
    private Bitmap selectedImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View Inflater = inflater.inflate(R.layout.fragment_create_petition, container, false);
        TextView headerDesc = Inflater.findViewById(R.id.headerDesc);
        headerDesc.setText("In 5 Simple Steps");
        TextView headerUser = Inflater.findViewById(R.id.headerUser);
        headerUser.setText("Create Your Own Petition");
        profilePic = Inflater.findViewById(R.id.profilePic);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        showUserProfile(firebaseUser);
        // Inflate the layout for this fragment


        if (firebaseUser != null) {
            // Set userID and name based on the current user
            userID = firebaseUser.getUid();
            name = firebaseUser.getDisplayName();

            // Create a User object
            currentUser = new User(userID, name, null);  // Set imageUrl to null for now, you can set it if needed
        }

        imagePlaceholder = Inflater.findViewById(R.id.imagePlaceholder);
        Button postButton = Inflater.findViewById(R.id.postButton);
        Button attachImageButton = Inflater.findViewById(R.id.attachImageButton);

        ImageButton backButton = Inflater.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.petitionMain, new RecentPetition());
                ft.commit();
            }
        });
        attachImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText titleBox = Inflater.findViewById(R.id.titleBox);
                EditText descBox = Inflater.findViewById(R.id.descriptionBox);
                EditText targetBox = Inflater.findViewById(R.id.targetBox);
                EditText councilBox = Inflater.findViewById(R.id.councilBox);
                String title = titleBox.getText().toString();
                String desc = descBox.getText().toString();
                String target = targetBox.getText().toString();
                String council = councilBox.getText().toString();

                if (title.isEmpty()) {
                    Toast.makeText(getContext(), "Title must not be blank!", Toast.LENGTH_SHORT).show();
                } else if (desc.isEmpty()) {
                    Toast.makeText(getContext(), "Description must not be blank!", Toast.LENGTH_SHORT).show();
                } else if (target.isEmpty()) {
                    Toast.makeText(getContext(), "Target must not be blank!", Toast.LENGTH_SHORT).show();
                } else if(imagePlaceholder.getTag().toString().equals("res/drawable/imageplaceholder.png")){
                    Toast.makeText(getContext(), "Image must not be blank!", Toast.LENGTH_SHORT).show();
                } else if (council.isEmpty()) {
                    Toast.makeText(getContext(), "Council must not be blank!", Toast.LENGTH_SHORT).show();
                }
                else {
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://heikommadapp-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Petition");
                    LocalDate currentDate = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    String formattedDate = currentDate.format(formatter);

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();
                    String imagePath = "PetitionImages/" + currentUser.getUsername() + "_" + formattedDate + "_" + council + "_" + title + "_" + Instant.now() + ".jpg";
                    StorageReference imageRef = storageRef.child(imagePath);

                    ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, byteArray);
                    byte[] data = byteArray.toByteArray();


                    UploadTask uploadTask = imageRef.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDatabase.push().setValue(new PetitionDetailItem(title, currentUser.getUsername(), formattedDate, council, Integer.parseInt(target), desc, currentUser.getUserId(), false, "[]", imagePath)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Successfully posted a petition!", Toast.LENGTH_SHORT).show();
                                        FragmentManager fm = getActivity().getSupportFragmentManager();
                                        FragmentTransaction ft = fm.beginTransaction();
                                        ft.replace(R.id.petitionMain, new RecentPetition());
                                        ft.commit();
                                    } else {
                                        Toast.makeText(getContext(), "Failed to post the petition, please try again!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });



                }
            }
        });

        return Inflater;
    }
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                displaySelectedImage(selectedImageUri);
            }
        }
    }

    private void displaySelectedImage(Uri selectedImageUri) {
        try {
            // Decode the image and set it to the image holder
            Bitmap bitmap = BitmapFactory.decodeStream(requireActivity().getContentResolver().openInputStream(selectedImageUri));
            selectedImage = bitmap;
            imagePlaceholder.setImageBitmap(bitmap);
            imagePlaceholder.setTag("NgMiaoMiao");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void showUserProfile(FirebaseUser firebaseUser){
        String userID=firebaseUser.getUid();

        DatabaseReference referenceProfile=FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetailsProfile readUserDetails=snapshot.getValue(ReadWriteUserDetailsProfile.class);
                if (readUserDetails!=null){
                    //Set user DP
                    Uri uri=firebaseUser.getPhotoUrl();

                    Picasso.with(getContext()).load(uri).into(profilePic);


                }else{
                    Toast.makeText(getContext(),"Something went wrong!",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),"Something went wrong!",Toast.LENGTH_LONG).show();
            }
        });
    }
}