package wisnu.brata.mynotes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ListNotesActivity extends Activity implements AdapterView.OnItemClickListener {

    Activity activity;
    ListView listView;
    ListAdapter adapter;
    private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    Button btnBack;
    DatabaseReference rootRef,demoRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notes);
        activity = this;
        btnBack = (Button) findViewById(R.id.btnBack);
        listView = findViewById(R.id.listview);
        listView.setOnItemClickListener(this);
        listView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        rootRef = FirebaseDatabase.getInstance().getReference();
        demoRef = rootRef.child("demo");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        getDataValue();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void getDataValue(){
        list = new ArrayList<HashMap<String, String>>();
        rootRef.child("demo")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 1;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String id = (String) snapshot.getKey();
                            String textTitle = (String) snapshot.child("textTitle").getValue();
                            String textInput = (String) snapshot.child("textInput").getValue();
                            String date = (String) snapshot.child("date").getValue();

                            HashMap<String, String> data = new HashMap<>();
                            data.put("key", id);
                            data.put("nomor", ""+i++);
                            data.put("textTitle", textTitle);
                            data.put("textInput", textInput);
                            data.put("date", date);
                            list.add(data);
                        }

                        adapter = new ListAdapter(activity, list,
                                R.layout.list_item, new String[]{"nomor","textTitle","textInput", "date"},
                                new int[]{R.id.nomor, R.id.textTitle,R.id.textInput, R.id.date});
                        Parcelable state = listView.onSaveInstanceState();
                        listView.setAdapter(adapter);
                        listView.onRestoreInstanceState(state);
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    public class ListAdapter extends SimpleAdapter {
        private Context mContext;
        public LayoutInflater inflater = null;

        public ListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            mContext = context;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            if (convertView == null)
                vi = inflater.inflate(R.layout.list_item, null);

            HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
            final Button btnDelete = vi.findViewById(R.id.delete);
            final TextView nomor = vi.findViewById(R.id.nomor);
            final TextView textTitle = vi.findViewById(R.id.textTitle);
            final TextView textInput = vi.findViewById(R.id.textInput);
            final TextView date = vi.findViewById(R.id.date);

            final String strKey = (String) data.get("key");
            final String strID = (String) data.get("nomor");
            final String strTextTitle = (String) data.get("textTitle");
            final String strTextInput = (String) data.get("textInput");
            final String strDate = (String) data.get("date");

            nomor.setText(strID);
            textTitle.setText(strTextTitle);
            textInput.setText(strTextInput);
            date.setText(strDate);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   Delete(strKey);
                }
            });

            return vi;
        }

    }

public void Delete(String keyID){
    Query applesQuery = demoRef.child(keyID);

    applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot dummySnapshot: dataSnapshot.getChildren()) {
                dummySnapshot.getRef().removeValue();
            }
            getDataValue();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e("delete", "onCancelled", databaseError.toException());
        }
    });
}


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        startActivity(new Intent(ListNotesActivity.this,MainActivity.class));
    }
}