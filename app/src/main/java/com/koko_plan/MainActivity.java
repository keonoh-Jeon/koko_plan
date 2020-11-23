package com.koko_plan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Todo_list_Item> todoListItems = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        RecyclerView recyclerView = findViewById(R.id.rv_todolist);
        recyclerView.setLayoutManager(layoutManager);

        TodoListAdapter todoListAdapter = new TodoListAdapter(todoListItems, this, (TodoListViewListener) this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(todoListAdapter);

    }
}