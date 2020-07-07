package ge.tsu.android.shopnotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import ge.tsu.android.data.Storage;
import ge.tsu.android.data.StorageImpl;
import ge.tsu.android.data.User;

public class RegisterActivity extends Activity {

    private EditText mUername;
    private EditText mPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mUername=findViewById(R.id.register_user_name);
        mPassword=findViewById(R.id.register_password);
    }
    public void registration(View view){
        Storage storage=new StorageImpl();

        String username=mUername.getText().toString();
        String userPassword=mPassword.getText().toString();
        if(!username.isEmpty()&&!userPassword.isEmpty()){
            User user=new User();
            user.setmName(username);
            user.setmPassword(userPassword);
            user.setmId(UUID.randomUUID().toString());

            User[] users= (User[]) storage.getObject(RegisterActivity.this, "Users_In_storage", User[].class);
            if(users!=null){
                int cvladi=0;
                for (User user1:users) {
                    if(user1.getmName().equals(user.getmName())&&user1.getmPassword().equals(user.getmPassword())) {
                        Toast.makeText(RegisterActivity.this, "User Already Exists", Toast.LENGTH_LONG).show();
                        cvladi++;
                    }
                }
                if(cvladi==0){
                    ArrayList<User> users1=new ArrayList<>();

                    users1.addAll(Arrays.asList(users));
                    users1.add(user);
                    storage.add(RegisterActivity.this,"Users_In_storage",users1);
                    Toast.makeText(RegisterActivity.this, username+" registered succesfully", Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(this,LoginActivity.class);
                    startActivity(intent);
                }
            }
            else{
                ArrayList<User> users1=new ArrayList<>();
                users1.add(user);
                storage.add(RegisterActivity.this,"Users_In_storage",users1);
                Toast.makeText(RegisterActivity.this, username+" registered succesfully", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(this,LoginActivity.class);
                startActivity(intent);
            }
        }else{
            Toast.makeText(RegisterActivity.this,"Fill all fields",Toast.LENGTH_LONG).show();
        }
    }
    public void alreadyRegistered(View view){
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
}
