package wisnu.brata.mynotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

public class WriteNotesActivity extends AppCompatActivity {

    Activity activity;
    EditText textTitle, textInput;
    Button submit;
    DatabaseReference rootRef, demoRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_notes);
        activity = this;

        textTitle = (EditText) findViewById(R.id.textTitle);
        textInput = (EditText) findViewById(R.id.textInput);
        submit = (Button) findViewById(R.id.btnSubmit);


        rootRef = FirebaseDatabase.getInstance().getReference();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strTextTitle = textTitle.getText().toString();
                String strtextInput = textInput.getText().toString();
                demoRef = rootRef.child("demo").push();
                demoRef.child("textTitle").setValue(strTextTitle);
                demoRef.child("textInput").setValue(strtextInput);
                demoRef.child("date").setValue(getDateTime());

                startActivity(new Intent(activity, ListNotesActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ResetField();
    }

    public void ResetField() {
        textTitle.setText("");
        textInput.setText("");
    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
