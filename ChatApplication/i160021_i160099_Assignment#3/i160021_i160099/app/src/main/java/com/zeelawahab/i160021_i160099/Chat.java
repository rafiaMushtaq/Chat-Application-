package com.zeelawahab.i160021_i160099;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Chat extends AppCompatActivity implements msgAdapter.onClickListener {

    Button sendButton;
    EditText messageArea;
    DatabaseReference reference1, reference2;
    FirebaseStorage storage;
    StorageReference storageReference;
    String user;
    String chatwith;
    RecyclerView mList;
    ArrayList<String> msgs;
    String Image_URL;
    private Button btnChoose, btnUpload;
    private ImageView imageView;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        msgs=new ArrayList<>();
        //Initialize Views
        btnChoose = (Button) findViewById(R.id.btnChoose);

        storage = FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        Intent i=getIntent();
        user=i.getStringExtra("user");
        chatwith=i.getStringExtra("chatwith");

        sendButton = findViewById(R.id.send);
        messageArea = findViewById(R.id.message);

        reference1 = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://i160021i160099.firebaseio.com/Messages/" + user + "_" + chatwith);
        reference2 = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://i160021i160099.firebaseio.com/Messages/" + chatwith + "_" + user);

        //getMsgs();
        //msgs.add("HEHE");

        mList=findViewById(R.id.msgbox);
        mList.setLayoutManager(new LinearLayoutManager(Chat.this));
        mList.setAdapter(new msgAdapter(msgs,Chat.this));
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });




        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if (!messageText.equals("")) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("sender", user);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                }
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Map<String, String> map = (Map) dataSnapshot.getValue();
                String message = map.get("message").toString();
                String userName = map.get("sender").toString();


                if (userName.equals(user)) {
                    addMessageBox("You:-\n" + message);
                } else {
                    addMessageBox(chatwith + ":-\n" + message);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

    public void addMessageBox(String message1) {
        msgs.add(message1);
        mList=findViewById(R.id.msgbox);
        mList.setLayoutManager(new LinearLayoutManager(Chat.this));
        mList.setAdapter(new msgAdapter(msgs,Chat.this));
    }

    @Override
    public void onClick(int position) {

    }

    public void getMsgs(){
        //Log.d("myTag", "hey1");
        DatabaseReference db= reference1;
        //Log.d("myTag", "hey2");
        db.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.d("myTag", "hey3");
                //msgs.clear();
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    //Log.d("myTag", "hey4");
                    Map<String, String> map = (Map) data.getValue();
                    //Log.d("myTag", "hey7");
                    String message = map.get("message").toString();
                    String userName = map.get("sender").toString();
                    String image=map.get("image").toString();
                    //Log.d("myTag",message);

                    if (userName.equals(user)) {
                        msgs.add("You:-\n" + message);
                    } else {
                        msgs.add(chatwith + ":-\n" + message);
                    }
                    Log.d("myTag", "hey5");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
              //  imageView.setImageBitmap(bitmap);
                uploadImage();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(Chat.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            //Uri d=taskSnapshot.getMetadata();
                            Image_URL=taskSnapshot.getMetadata().toString();
                            Log.d("url",Image_URL);

                            Map<String, String> map = new HashMap<String, String>();

                            map.put("message", "~"+Image_URL);
                            map.put("sender", user);
                            reference1.push().setValue(map);
                            reference2.push().setValue(map);
                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Chat.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });

            //Image_URL=ref.getDownloadUrl().toString();

            //Log.d("url",Image_URL);

        }
    }
}
