package com.koko_plan.member;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.datatransport.cct.internal.LogEvent;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kakao.sdk.auth.AuthCodeClient;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.common.KakaoSdk;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;
import com.koko_plan.main.MainActivity;
import com.koko_plan.R;
import com.koko_plan.sub.MySoundPlayer;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class Singup extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth = null;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        updateKakaoLoginUi();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.emailsignup).setOnClickListener(btnClickListener);
        findViewById(R.id.gotoLogin).setOnClickListener(btnClickListener);
        findViewById(R.id.btn_sign_in_goolgle).setOnClickListener(btnClickListener);

        findViewById(R.id.btn_sign_in_kakao).setOnClickListener(btnClickListener);

        MySoundPlayer.initSounds(getApplicationContext());
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    private void updateKakaoLoginUi() {
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                if(user!=null){
                    Log.d(TAG, "invoke: 카카오 id " + user.getId());
                    Log.d(TAG, "invoke: 카카오 nickname " + user.getKakaoAccount().getProfile().getNickname());
                    Log.d(TAG, "invoke: 카카오 id " + user.getKakaoAccount().getProfile().getProfileImageUrl());

//                    Glide.with(profileImage).load(user.getKakaoAccount().getProfile().getThumbnailImageUrl()).circleCrop().into(profileImaget);

//                    myStartActivity(MainActivity.class);

                } else {
                    Log.e(TAG, "invoke: 카카오 유저정보 없슴");

                }
                return null;
            }
        });
    }

    protected void onPause() {
        super.onPause();
        System.gc();
    }

    private final Button.OnClickListener btnClickListener = new View.OnClickListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.emailsignup:
                    MySoundPlayer.play(MySoundPlayer.CLICK);
                    myStartActivity(Singupmail.class);
                    break;
                case R.id.gotoLogin:
                    MySoundPlayer.play(MySoundPlayer.CLICK);
                    myStartActivity(Login.class);
                    break;
                case R.id.btn_sign_in_goolgle:
                    MySoundPlayer.play(MySoundPlayer.CLICK);
                    if(mGoogleSignInClient != null) {
                    mGoogleSignInClient.signOut();}
                    signIn();
                    break;
                case R.id.btn_sign_in_kakao:
                    MySoundPlayer.play(MySoundPlayer.CLICK);
                    signInwithkakao();
                    break;
            }
        }

        private void signIn() {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }

        private void signInwithkakao() {
            Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
                @Override
                public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                    Log.e(TAG, "invoke: 카카오 토큰" + oAuthToken.getAccessToken());
                    if(oAuthToken != null){
                        oAuthToken.getAccessToken();
//                        updateKakaoLoginUi();
                        firebaseAuthWithKakao(oAuthToken.getAccessToken());
                    }
                    if(throwable != null){

                    }
                    return null;
                }
            };

            if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(Singup.this)){
                UserApiClient.getInstance().loginWithKakaoTalk(Singup.this, callback);
            } else {
                UserApiClient.getInstance().loginWithKakaoAccount(Singup.this, callback);
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        updateUI(user);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                firebaseAuthWithGoogle(account);
            } catch (ApiException ignored) {
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            myStartActivity(MainActivity.class);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }

    private void firebaseAuthWithKakao(String accessToken) {
        Log.e(TAG, "firebaseAuthWithKakao: 카카오 " + accessToken  );
        mAuth.signInWithCustomToken(accessToken)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCustomToken:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            myStartActivity(MainActivity.class);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCustomToken:failure", task.getException());
                            Toast.makeText(Singup.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
    }

    private void myStartActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
}
