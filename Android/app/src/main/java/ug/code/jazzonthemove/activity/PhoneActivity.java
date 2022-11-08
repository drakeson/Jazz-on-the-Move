package ug.code.jazzonthemove.activity;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import ug.code.jazzonthemove.MainActivity;
import ug.code.jazzonthemove.R;
import ug.code.jazzonthemove.app.Session;

import java.util.concurrent.TimeUnit;

public class PhoneActivity extends AppCompatActivity {

    private EditText phoneText;
    private LinearLayout phoneLayout,codeLayout;
    private ProgressBar phoneBar,codeBar;
    private Button sendButton,verifyButton;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    FirebaseAuth.AuthStateListener mAuthListener;
    private Pinview smsCode;
    private FirebaseAuth mAuth;
    private CountryCodePicker ccp;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        session = new Session(this);


        mAuth = FirebaseAuth.getInstance();

        smsCode = findViewById(R.id.sms_code);
        phoneText = findViewById(R.id.phoneText);

        ccp = findViewById(R.id.ccp);
        phoneLayout =  findViewById(R.id.phoneLayout);
        codeLayout =  findViewById(R.id.codeLayout);

        phoneBar = findViewById(R.id.phoneBar);
        codeBar = findViewById(R.id.codeBar);

        sendButton = findViewById(R.id.sendButton);
        verifyButton = findViewById(R.id.verifyButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                phoneBar.setVisibility(View.VISIBLE);
                phoneText.setEnabled(false);
                sendButton.setEnabled(false);

                ccp.registerCarrierNumberEditText(phoneText);

                String phoneNumber = "+" + ccp.getFullNumber();

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneNumber,        // Phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        PhoneActivity.this,
                        mCallBacks);

            }
        });

        //When you press verify code button

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeBar.setVisibility(View.VISIBLE);
                String code = smsCode.getValue();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
                signInWithPhoneAuthCredential(credential);
            }
        });

        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(PhoneActivity.this,"error in verification",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                mResendToken = token;
                phoneLayout.setVisibility(View.GONE);
                codeLayout.setVisibility(View.VISIBLE);
            }

        };

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = task.getResult().getUser();

                            Intent LoggedIn = new Intent(PhoneActivity.this, EditProfileActivity.class);
                            startActivity(LoggedIn);
                            finish();
                        } else {
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(PhoneActivity.this,"error",Toast.LENGTH_LONG).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }


    private void launchHomeScreen() {
        session.setFirstTimeLaunch(false);
        session.setLoggedIn(false);
        startActivity(new Intent(PhoneActivity.this, MainActivity.class));
        finish();
    }

}
