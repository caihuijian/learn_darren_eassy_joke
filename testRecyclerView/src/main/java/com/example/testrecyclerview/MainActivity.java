package com.example.testrecyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("native-mylib");
        System.loadLibrary("native-myclib");
    }

    public native String myStringFromJNI();

    public native String stringFromJNI();

    public native String testString1();

    public native String testString2();

    public native String testCString();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.test);
        tv.setText(myStringFromJNI() + stringFromJNI() + testString1() + testCString());

        recyclerView = findViewById(R.id.recycler);
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            users.add(new User("hjcai" + i, i, "http://goo.gl/gEgYUd"));
        }
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(users, this, this.getLayoutInflater());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.addItemDecoration(new RecyclerViewItemDecoration(ContextCompat.getDrawable(this, R.drawable.recycler_view_divider), layoutManager.getOrientation()));
        adapter.setItemClickListener((position, holder) -> Toast.makeText(MainActivity.this, holder.mLeftTv.getText().toString() + " " + position, Toast.LENGTH_SHORT).show());
        recyclerView.setAdapter(adapter);
    }
}