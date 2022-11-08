package ug.code.jazzonthemove.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.asksira.bsimagepicker.BSImagePicker;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import ug.code.jazzonthemove.MainActivity;
import ug.code.jazzonthemove.R;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ug.code.jazzonthemove.app.Session;

public class EditProfileActivity extends AppCompatActivity implements BSImagePicker.OnSingleImageSelectedListener {

    private EditText mUsername, mBio;
    FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private File actualImage;
    private File compressedImage;
    private StorageTask uploadTask;
    StorageReference storageRef;
    private ImageView mPro;
    String mProImgs = "";
    private Button button;
    private ProgressDialog progressDialog;
    FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        button = findViewById(R.id.sendButton);
        mPro = findViewById(R.id.profile_image);
        mUsername = findViewById(R.id.userN);
        mBio = findViewById(R.id.userBio);
        session = new Session(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public  void  onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    checkUser(user.getUid());
                } else {
                    Intent intent = new Intent(EditProfileActivity.this, PhoneActivity.class);
                    startActivity(intent);
                    finish();
                }
            }


        };



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUsername.getText().toString().length() < 3) {
                    Toasty.error(getApplicationContext(), "Enter Username", Toast.LENGTH_SHORT).show();
                } else if (mBio.getText().toString().length() < 3) {
                    Toasty.error(getApplicationContext(), "Enter Bio", Toast.LENGTH_SHORT).show();
                } else if (mProImgs.equals("")){
                    Toasty.error(getApplicationContext(), "Attach Profile Image", Toast.LENGTH_SHORT).show();
                }else {
                    sendInfo(mUsername.getText().toString(), mBio.getText().toString());
                }
            }
        });

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        storageRef = FirebaseStorage.getInstance().getReference("images");
    }

    public void sendInfo(String name, String bio){
        progressDialog.setMessage("Updating Account...");
        showDialog();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", firebaseUser.getUid());
        map.put("username", name.toLowerCase());
        map.put("email", bio.toLowerCase());
        map.put("phone", firebaseUser.getPhoneNumber());
        map.put("imageurl", mProImgs);
        reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    hideDialog();
                    session.setFirstTimeLaunch(false);
                    session.setLoggedIn(false);
                    Toasty.success(getApplicationContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public void photo(View view) {
        BSImagePicker pickerDialog = new BSImagePicker
                .Builder("com.escort.ornies.fileprovider")
                .build();
        pickerDialog.show(getSupportFragmentManager(), "picker");
    }

    @SuppressLint("CheckResult")
    @Override
    public void onSingleImageSelected(Uri uri, String tag) {
        try {
            actualImage = FileUtil.from(this, uri);
            new Compressor(this)
                    .compressToFileAsFlowable(actualImage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<File>() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void accept(File file) {
                            compressedImage = file;
                            Glide.with(EditProfileActivity.this)
                                    .load(BitmapFactory.decodeFile(compressedImage.getAbsolutePath()))
                                    .apply(RequestOptions.circleCropTransform())
                                    .into(mPro);
                            uploadImage();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void uploadImage(){

        if (compressedImage != null){
            final StorageReference fileReference = storageRef.child(compressedImage.getAbsolutePath());
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            //System.out.println(fileReference);
            System.out.println(compressedImage.getAbsolutePath());
            System.out.println(Uri.fromFile(compressedImage));

            uploadTask = fileReference.putFile(Uri.fromFile(compressedImage));
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) throw task.getException();
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String miUrlOk = downloadUri.toString();
                        mProImgs = miUrlOk;
                        reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
                        HashMap<String, Object> map1 = new HashMap<>();
                        map1.put("id", firebaseUser.getUid());
                        map1.put("username", mUsername.getText().toString().toLowerCase());
                        map1.put("email", mBio.getText().toString());
                        map1.put("phone", firebaseUser.getPhoneNumber());
                        map1.put("imageurl", miUrlOk);
                        reference.updateChildren(map1);
                        Toast.makeText(getApplicationContext(), "Profile Photo Updated", Toast.LENGTH_SHORT).show();

                    } else {
                        hideDialog();
                        Toast.makeText(EditProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    hideDialog();
                    Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println(e.getMessage());
                }
            });

        } else {
            Toast.makeText(EditProfileActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkUser(String userId){
        reference = FirebaseDatabase.getInstance().getReference("users");
        reference.orderByChild("id").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot);
                if (dataSnapshot.exists()) {
                    session.setFirstTimeLaunch(false);
                    session.setLoggedIn(false);
                    Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError);
                Toast.makeText(EditProfileActivity.this, "SetUp Profile", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditProfileActivity.this, PhoneActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

}
