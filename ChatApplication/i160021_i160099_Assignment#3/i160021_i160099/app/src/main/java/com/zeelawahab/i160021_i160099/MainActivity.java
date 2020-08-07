package com.zeelawahab.i160021_i160099;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements clistAdapter.onClickListener{
    ArrayList<String> persons;
    ArrayList<String> Searchpersons;
    ArrayList<String> Searchlastmsg;
    ArrayList<String> lastmsg;
    ArrayList<String> emails;
    public String userEmail;
    public String username;
    DatabaseReference ref;
    DatabaseReference ref1;
    RecyclerView clist;
    EditText searchET;
    String lastTmp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        persons=new ArrayList<>();
        lastmsg=new ArrayList<>();
        Searchpersons=new ArrayList<>();
        Searchlastmsg=new ArrayList<>();
        emails=new ArrayList<>();
        Intent intent=getIntent();
        userEmail=intent.getStringExtra("email");

        ref= FirebaseDatabase.getInstance().getReferenceFromUrl("https://i160021i160099.firebaseio.com/Users");
        ref1=FirebaseDatabase.getInstance().getReferenceFromUrl("https://i160021i160099.firebaseio.com/Emails");



        clist=findViewById(R.id.users);
        clist.setLayoutManager(new LinearLayoutManager(this));

        searchET=findViewById(R.id.searchET);

        searchET.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String sname=searchET.getText().toString();
                if (sname.matches("")){
                    clist.setAdapter(new clistAdapter(persons,lastmsg,MainActivity.this));
                }
                else{
                    getUsersSearch(sname);
                    clist.setAdapter(new clistAdapter(Searchpersons,Searchlastmsg,MainActivity.this));
                }

            }
        });
    }

    @Override
    public void onClick(int position) {
        String sname=searchET.getText().toString();
        String chatwith;
        if (sname.matches("")){
            chatwith=persons.get(position);
        }
        else{
            chatwith=Searchpersons.get(position);
        }

        Intent i=new Intent(MainActivity.this,Chat.class);
        i.putExtra("user",username);
        i.putExtra("chatwith",chatwith);
        startActivity(i);
    }


    public void getUsers(){
        persons.clear();
        DatabaseReference db= ref;
        db.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    persons.add(data.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public void getEmails(){
        emails.clear();
        DatabaseReference db= ref1;
        db.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    emails.add(data.getValue().toString());
                }
                username=persons.get(emails.indexOf(userEmail));
                persons.remove(username);
                emails.remove(userEmail);

                getMsgs();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public void getMsgs(){
        lastmsg.clear();
        for(int i = 0; i < persons.size(); i++){
            lastTmp="Nothing";
            DatabaseReference db = FirebaseDatabase.getInstance()
                    .getReferenceFromUrl("https://i160021i160099.firebaseio.com/Messages/" + username + "_" + persons.get(i));
            db.addValueEventListener(new ValueEventListener(){
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot data: dataSnapshot.getChildren()) {
                        Map<String, String> map = (Map) data.getValue();
                        String message = map.get("message").toString();
                        String userName = map.get("sender").toString();
                        lastTmp=message;
                        Log.d("My",lastTmp);
                    }
                    if(lastTmp.matches("Nothing")){
                        lastmsg.add("Start Conversation");
                    }
                    else{
                        lastmsg.add(lastTmp);
                        lastTmp="Nothing";
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("My","failed");
                }

            });

        }
    }

    public void getUsersSearch(String n){
        if(persons.contains(n)){
            Searchpersons.clear();
            Searchpersons.add(n);
            Searchlastmsg.add(lastmsg.get(persons.indexOf(n)));
        }
        else{
            //Toast.makeText(getApplicationContext(), "User Not Found", Toast.LENGTH_LONG).show();
            Searchpersons.clear();
            Searchlastmsg.clear();
            }

    }

    @Override
    protected void onStart() {
        super.onStart();
        getUsers();
        getEmails();

        Handler handler = new Handler();
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                clist.setAdapter(new clistAdapter(persons, lastmsg, MainActivity.this));
            }
        };
        handler.postDelayed(runnableCode, 5000);


    }

}


