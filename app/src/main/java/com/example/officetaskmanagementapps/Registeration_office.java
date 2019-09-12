package com.example.officetaskmanagementapps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registeration_office extends AppCompatActivity {


    AppCompatEditText editText,editText1;
    AppCompatButton button;
    AppCompatTextView textView;

    private FirebaseAuth auth;

    private ProgressDialog mdailog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration_office);


        editText=(AppCompatEditText) findViewById(R.id.email_sp);

        editText1=(AppCompatEditText) findViewById(R.id.pass_sp);

        button=(AppCompatButton) findViewById(R.id.signup_btn);

        textView=(AppCompatTextView) findViewById(R.id.login_sp);

        auth=FirebaseAuth.getInstance();


        mdailog=new ProgressDialog(this);



        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Registeration_office.this,MainActivity.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email=editText.getText().toString().trim();
                String pass=editText1.getText().toString().trim();

                if (TextUtils.isEmpty(email)){

                    editText.setError("Required Field....");
                    return;
                }

                if (TextUtils.isEmpty(pass)){

                    editText1.setError("Required Field....");
                    return;
                }


                mdailog.setMessage("Processing....");
                mdailog.show();
                auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (task.isSuccessful()){

                              Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_LONG).show();



                            mdailog.dismiss();
                        }

                        else{
                            Toast.makeText(getApplicationContext(),"Problem",Toast.LENGTH_LONG).show();

                            mdailog.dismiss();

                        }

                    }
                });

            }
        });



    }
}
