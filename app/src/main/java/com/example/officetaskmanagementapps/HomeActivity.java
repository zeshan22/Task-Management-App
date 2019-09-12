package com.example.officetaskmanagementapps;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.officetaskmanagementapps.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;


public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private FloatingActionButton fabt;


    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    RecyclerView recyclerView;

    // input update field

    private AppCompatEditText edit1,edit2;
    private AppCompatButton  but1,but2;

    // Vatriables

    private String title;
    private String note;
    private String post_key;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        edit1=(findViewById(R.id.edit_txt1_update));
        edit2=(findViewById(R.id.edit_txt2_update));

        but1=(findViewById(R.id.button1_update));
        but2=(findViewById(R.id.button2_update));



        toolbar = (Toolbar) findViewById(R.id.toolbar);



        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Your Task App");


        fabt = (FloatingActionButton) findViewById(R.id.fab_btn);

        // Firebase Databse....
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser mUser = mAuth.getCurrentUser();

        String uiD = mUser.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("TaskNote");

        mDatabase.keepSynced(true);


        // Recycler view .....

        // RecyclerView recyclerView=findViewById(R.id.recycler_1);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_1);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        fabt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final AlertDialog.Builder myDailog = new AlertDialog.Builder(HomeActivity.this);

                LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);

                View myview = inflater.inflate(R.layout.custominputfield, null);

                myDailog.setView(myview);

                final AlertDialog dd = myDailog.create();

                dd.show();


                final AppCompatEditText title = myview.findViewById(R.id.edit_txt1);
                final AppCompatEditText pass = myview.findViewById(R.id.edit_txt2);

                AppCompatButton btn = myview.findViewById(R.id.button1_id);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String mTitle = title.getText().toString().trim();

                        String mPass = pass.getText().toString().trim();

                        if (TextUtils.isEmpty(mTitle)) {

                            title.setError("Required Field....");
                            return;
                        }
                        if (TextUtils.isEmpty(mPass)) {

                            pass.setError("Required Field....");
                            return;
                        }


                        String id = mDatabase.push().getKey();

                        String datee = DateFormat.getDateInstance().format(new Date());

                        Data data = new Data(mTitle, mPass, datee, id);

                        mDatabase.child(id).setValue(data);

                        Toast.makeText(getApplicationContext(), "Data insert", Toast.LENGTH_LONG).show();


                        dd.dismiss();

                    }
                });

            }
        });

    }

    protected void onStart() {

        super.onStart();

        FirebaseRecyclerAdapter<Data, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>
                (
                        Data.class,
                        R.layout.item_data,
                        MyViewHolder.class,
                        mDatabase
                ) {
            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, final Data model, final int position) {

                viewHolder.setTitle(model.getTitle());
                viewHolder.setNote(model.getNote());
                viewHolder.setDate(model.getDate());

                viewHolder.myView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        post_key=getRef(position).getKey();
                        title=model.getTitle();
                        note=model.getNote();


                        updateData();
                    }
                });


            }
        };
        recyclerView.setAdapter(adapter);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        View myView;

        public MyViewHolder(View itemView) {

            super(itemView);
            myView = itemView;
        }

        public void setTitle(String title) {
            TextView mtitle = myView.findViewById(R.id.textview_02);
            mtitle.setText(title);
        }

        public void setNote(String note) {

            TextView mNote = myView.findViewById(R.id.textview_03);
            mNote.setText(note);
        }

        public void setDate(String date) {

            TextView mNote = myView.findViewById(R.id.textview_01);
            mNote.setText(date);
        }


    }


    public void  updateData(){

        final AlertDialog.Builder myDailog=new AlertDialog.Builder(HomeActivity.this);
        LayoutInflater layoutInflater=LayoutInflater.from(HomeActivity.this);

        View myView= layoutInflater.inflate(R.layout.update_inputfield,null);
        myDailog.setView(myView);

        AlertDialog dialog=myDailog.create();

        edit1=myView.findViewById(R.id.edit_txt1_update);
        edit2=myView.findViewById(R.id.edit_txt2_update);


        but1=myView.findViewById(R.id.button1_update);
        but2=myView.findViewById(R.id.button2_update);

        edit1.setText(title);
        edit1.setSelection(title.length());
        edit2.setText(note);
        edit2.setSelection(note.length());




        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                title=edit1.getText().toString().trim();
                note=edit2.getText().toString().trim();

                String mDate=DateFormat.getDateInstance().format(new Date());

                Data data=new Data(title,note,mDate,post_key);

                mDatabase.child(post_key).setValue(data);


                updateData();




            }
        });




        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase.child(post_key).removeValue();

            }
        });





        dialog.show();

    }


    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.mainmenu, menu);

        return super.onCreateOptionsMenu(menu);


    }


    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.logout:
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
        }

        return  super.onOptionsItemSelected(item);
    }

}