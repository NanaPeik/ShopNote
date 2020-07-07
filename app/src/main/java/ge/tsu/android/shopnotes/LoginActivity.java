package ge.tsu.android.shopnotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import ge.tsu.android.data.Storage;
import ge.tsu.android.data.StorageImpl;
import ge.tsu.android.data.User;

public class LoginActivity extends Activity
{
    private EditText mUername;
    private EditText mPassword;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUername=findViewById(R.id.login_username);
        mPassword=findViewById(R.id.login_pass);

    }
    public void signIn(View view){
        Storage storage=new StorageImpl();
        String username = mUername.getText().toString();
        String password = mPassword.getText().toString();
        int cvladi=0;
        if(!username.isEmpty()&&!password.isEmpty()){
            User[] users= (User[]) storage.getObject(this, "Users_In_storage", User[].class);
            if(users!=null){
                for (User user1:users) {
                    if(user1.getmName().equals(username)&&user1.getmPassword().equals(password)) {
                        Toast.makeText(this, "Hello "+username, Toast.LENGTH_LONG).show();
                        cvladi++;
                        storage.add(this,"current_User",user1);
                        Intent intent=new Intent(this,NoteActivity.class);
                        startActivity(intent);
                    }
                }
                if(cvladi==0){
                    Toast.makeText(this, "This User is not Registered", Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(this, "This User is not Registered", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this,"Fill all fields",Toast.LENGTH_LONG).show();

        }

    }
    public void register(View view){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
}
