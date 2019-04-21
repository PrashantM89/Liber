package org.app.liber;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.app.liber.helper.DatabaseHelper;

public class UserLogin extends Activity {
    EditText username, email, mobile;
    TextView loginBttn;
    AlertDialog alert;
    UserSessionManager session;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);
        databaseHelper = new DatabaseHelper(getApplicationContext());


        session = new UserSessionManager(getApplicationContext());
        username = (EditText)findViewById(R.id.name_edttxt_id);
        email = (EditText)findViewById(R.id.email_edttxt_id);
        mobile = (EditText)findViewById(R.id.mobile_edttxt_id);
        loginBttn = (TextView)findViewById(R.id.login_id);
//        Toast.makeText(getApplicationContext(), "User Login Status "+session.)

        loginBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username1= username.getText().toString();
                String mobile1 = mobile.getText().toString();
                String email1 = email.getText().toString();

                //if(username1.length()>0 && mobile1.length()>0 && email1.length()>0){
                  //  if(username1.equals("test") && mobile1.equals("123") && email1.equals("test")){
                        session.createLoginSession(username1,email1,mobile1);
                        Intent i = new Intent(getApplicationContext(), MainLiberActivity.class);
                        startActivity(i);
                        finish();
//                    }else {
//                        Toast.makeText(getApplicationContext(),"Login Failed. Username or Password is incorrect.",Toast.LENGTH_LONG).show();
//                    }
//                }else{
//                    Toast.makeText(getApplicationContext(),"Login Failed. Enter Username/Password.",Toast.LENGTH_LONG).show();
//                }

            }
        });

    }
}
