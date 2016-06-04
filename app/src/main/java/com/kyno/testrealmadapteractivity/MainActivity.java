package com.kyno.testrealmadapteractivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RealmConfiguration realmConfig = new RealmConfiguration.Builder(getApplicationContext()).build();
        realm = Realm.getInstance(realmConfig);

        final String[] names = {"ab", "bc", "cd", "de", "ef"};

        if (realm.where(Pet.class).findAll().size() == 0) {

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    for (int i = 0; i < 5; i++) {
                        realm.copyToRealmOrUpdate(new Pet(names[i]));
                    }

                }
            });
        }
        final RealmBaseAdapter<Pet> adapter = new RealmBaseAdapter<Pet>(this, realm.where(Pet.class).findAll()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                if (null == convertView) {
                    convertView = new TextView(context);
                    convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
                    convertView.setPadding(20, 20, 20, 20);
                }
                TextView textView = (TextView) convertView;
                textView.setText(getItem(position).getName());
                return textView;
            }

        };


        ListView listView = (ListView) findViewById(R.id.listview);
        assert null != listView;
        listView.setAdapter(adapter);

        Button addButton = (Button) findViewById(R.id.add_btn);
        assert null != addButton;

        final EditText editText = (EditText) findViewById(R.id.edit_text);
        assert null != editText;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(new Pet(editText.getText().toString()));
                        editText.setText("");
                    }
                });
            }
        });

        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                RealmConfiguration realmConfig = new RealmConfiguration.Builder(getApplicationContext()).build();
                Realm realm = Realm.getInstance(realmConfig);
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(new Pet(String.format(Locale.getDefault(), "%d", SystemClock.currentThreadTimeMillis())));
                    }
                });
                realm.close();
            }
        }, 0, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
