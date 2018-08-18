package com.jaswan26.bikesforrent;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private DatabaseReference mUserRef;
    private DatabaseReference mRootRef;
    private DatabaseReference mChatRef;
    private FirebaseAuth mAuth;
    private String mCurrentUserId;
    private String mCurrentUserName="";
    private ShareActionProvider mShareActionProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView UserIdT=(TextView)findViewById(R.id.userId);
        final TextView UserName=(TextView)findViewById(R.id.dis_name) ;


        mAuth = FirebaseAuth.getInstance();
        mChatRef = FirebaseDatabase.getInstance().getReference().child("Chats");
        mRootRef = FirebaseDatabase.getInstance().getReference();


        if (mAuth.getCurrentUser() != null) {
            mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
            mCurrentUserId = mAuth.getCurrentUser().getUid();

        }
        else
        {
            sendToStart();
        }



        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCurrentUserName =dataSnapshot.child("name").getValue().toString();
                Log.d("MainActivity",mCurrentUserName+" jkh");

                UserName.setText(mCurrentUserName+"");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
             mCurrentUserName= ""+databaseError.getCode();
            }
        });
        UserIdT.setText(mCurrentUserId);


    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if(currentUser==null)
        {
            sendToStart();
        }


    }


    private void sendToStart() {
        Intent startIntent = new Intent(MainActivity.this,StartActivity.class);
        startActivity(startIntent);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.main_logout_btn)
        {
            FirebaseAuth.getInstance().signOut();
            sendToStart();
        }
        return true;
    }



}