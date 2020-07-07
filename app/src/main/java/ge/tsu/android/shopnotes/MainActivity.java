package ge.tsu.android.shopnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ge.tsu.android.data.Storage;
import ge.tsu.android.data.StorageImpl;
import ge.tsu.android.data.User;
import ge.tsu.android.fragments.FragmentAddNote;

public class MainActivity extends Activity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Storage storage=new StorageImpl();
        Object object=storage.getObject(MainActivity.this,"current_User", User.class);
        if(object!=null){
            Intent intent=new Intent(MainActivity.this, NoteActivity.class);
            startActivity(intent);
        }
        setContentView(R.layout.activity_main);

        findViewById(R.id.sign_In_Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.sign_Up_Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
