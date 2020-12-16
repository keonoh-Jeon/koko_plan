package com.koko_plan.member;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.koko_plan.R;

public class PasswordReset extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passreset);
        init();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    private void init (){

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.passreset).setOnClickListener(OnClickListener);
        findViewById(R.id.passcheck).setOnClickListener(OnClickListener);
    }

    protected void onPause() {
        super.onPause();
        System.gc();
    }

    private View.OnClickListener OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.passreset:
//                    MySoundPlayer.play(MySoundPlayer.CLICK);
                    passreset();
                    break;

                case R.id.passcheck:
//                    MySoundPlayer.play(MySoundPlayer.CLICK);
                    myStartActivity(Login.class);
                    break;
            }
        }
    };

    private void myStartActivity(Class c) {

        Intent intent = new Intent(this, c);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    private void passreset() {
        String password = ((EditText) findViewById(R.id.passwordResetText)).getText().toString();

        if (password.length() > 0) {

            mAuth.sendPasswordResetEmail(password)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                startToast("Check your Email box.");

                            }
                        }
                    });
        }else{
            startToast("please, write Email.");
        }
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }
}
