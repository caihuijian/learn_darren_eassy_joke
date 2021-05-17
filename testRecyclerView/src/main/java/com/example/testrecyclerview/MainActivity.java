package com.example.testrecyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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