package com.example.officetaskmanagementapps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {


    AppCompatTextView textView;

    AppCompatEditText editText,editText1;

    AppCompatButton button;

    FirebaseAuth mauth;

    private ProgressDialog mDailog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mauth=FirebaseAuth.getInstance();

        if (mauth.getCurrentUser()!=null){

            startActivity(new Intent(getApplicationContext(),HomeActivity.class));

        }

        mDailog=new ProgressDialog(this);
        textView=(AppCompatTextView) findViewById(R.id.signup_txt);

        editText=(AppCompatEditText) findViewById(R.id.login_em);

        editText1=(AppCompatEditText) findViewById(R.id.login_pass);

        button=(AppCompatButton) findViewById(R.id.login_btn);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(),Registeration_office.class);


                startActivity(intent);
            }
        });




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String eEmail=editText.getText().toString();
                String ePass= editText1.getText().toString();

                if(TextUtils.isEmpty(eEmail)){

                    editText.setError("Required Field...");


                }

                if(TextUtils.isEmpty(ePass)){

                    editText1.setError("Required Field...");


                }

                mDailog.setMessage("Processing...");
                mDailog.show();

                mauth.signInWithEmailAndPassword(eEmail,ePass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            Toast.makeText(getApplicationContext(),"Successfully Login",Toast.LENGTH_LONG).show();
                            mDailog.dismiss();

                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        }

                        else {


                            Toast.makeText(getApplicationContext(),"Unable to login",Toast.LENGTH_LONG).show();
                        }


                    }
                });

            }
        });



    }

}
