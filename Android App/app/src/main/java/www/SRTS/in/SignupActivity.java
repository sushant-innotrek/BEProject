package www.SRTS.in;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignupActivity extends AppCompatActivity {

    private EditText Mobile,Email, Name, Password, CPassword;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference database;
    private NewUser newUser ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Mobile = findViewById(R.id.editMobile);
        Name = findViewById(R.id.editAadhaar);
        Email = findViewById(R.id.editEmail);
        Password = findViewById(R.id.editPass);
        CPassword = findViewById(R.id.editconfirm);

        //NewUser
        newUser = new NewUser();
        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

    }

    public void Register(View view) {
        final String mobile = Mobile.getText().toString();
        final String name = Name.getText().toString();
        final String email = Email.getText().toString();
        String password = Password.getText().toString();
        String cpassword = CPassword.getText().toString();

        newUser.setEmail(email);
        newUser.setMobile(mobile);
        newUser.setName(name);
        newUser.setWalletBalance(100.0);

        if(TextUtils.isEmpty(mobile) || TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(cpassword) ){
            Toast.makeText(SignupActivity.this, "All Fields Compulsary", Toast.LENGTH_LONG).show();
        }
        else{
            if(password.equals(cpassword)){
               Signup(email,password);
            }
            else{
                Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void Signup(String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.e("UserID",firebaseAuth.getUid());
                    database.child("Users").child(firebaseAuth.getUid()).setValue(newUser);
                    Toast.makeText(SignupActivity.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                    finish();
                }
                else
                {
                    Toast.makeText(SignupActivity.this, "Problem in registering..."+task.getException(), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                    finish();
                }
            }
        });

    }

    public void Redirect(View view) {
        startActivity(new Intent(SignupActivity.this,LoginActivity.class));
    }
}
