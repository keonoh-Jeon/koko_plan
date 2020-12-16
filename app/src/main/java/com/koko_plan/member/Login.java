package com.koko_plan.member;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.koko_plan.MainActivity;
import com.koko_plan.R;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    protected void onPause() {
        super.onPause();
        System.gc();
    }

    private void init(){

        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.login).setOnClickListener(OnClickListener);
        findViewById(R.id.reset).setOnClickListener(OnClickListener);

    }

    private View.OnClickListener OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login:
//                    MySoundPlayer.play(MySoundPlayer.CLICK);
                    pd = ProgressDialog.show(Login.this, "Log-in", "Loading......");
                    login();
                    break;
                case R.id.reset:
//                    MySoundPlayer.play(MySoundPlayer.CLICK);
                    myStartActivity(PasswordReset.class);
                    break;
            }
        }
    };

    private void login() {
        String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();

        if (email.length() > 0 && password.length() > 0) {

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                startToast("Success log in");
                                myStartActivity(MainActivity.class);
                            } else {
                                if (task.getException() != null) {
                                    pd.dismiss();
                                    startToast(task.getException().toString());
                                    updateUI();
                                }
                            }
                        }
                    });
        }
    }

    private void updateUI() {
    }

    private void myStartActivity(Class c){

        Intent intent = new Intent(this, c);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, Singup.class);
        startActivity(intent);
        finish();
    }
}
