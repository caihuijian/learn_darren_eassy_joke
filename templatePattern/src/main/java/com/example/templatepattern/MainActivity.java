package com.example.templatepattern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerview);


        //设置固定大小
//        recyclerView_one.setHasFixedSize(true);
        //创建线性布局
        mLayoutManager = new LinearLayoutManager(this);
        //垂直方向
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        //给RecyclerView设置布局管理器
        recyclerView.setLayoutManager(mLayoutManager);
        //创建适配器，并且设置
        mAdapter = new TestRecyclerAdapter(this);
        recyclerView.setAdapter(mAdapter);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(recyclerView.getContext().getDrawable(R.drawable.listview_divider));
        recyclerView.addItemDecoration(divider);
    }




    static class TestRecyclerAdapter extends RecyclerView.Adapter<TestRecyclerAdapter.ViewHolder>{

        private LayoutInflater mInflater;
        private String[] mTitles=null;

        public TestRecyclerAdapter(Context context){
            this.mInflater=LayoutInflater.from(context);
            this.mTitles=new String[20];
            for (int i=0;i<20;i++){
                int index=i+1;
                mTitles[i]="item"+index;
            }
        }
        /**
         * item显示类型
         * @param parent
         * @param viewType
         * @return
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=mInflater.inflate(R.layout.item_recycler_layout,parent,false);
//            view.setBackgroundColor(Color.GRAY);
            ViewHolder viewHolder=new ViewHolder(view);
            return viewHolder;
        }
        /**
         * 数据的绑定显示
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.item_tv.setText(mTitles[position]);
        }

        @Override
        public int getItemCount() {
            return mTitles.length;
        }

        //自定义的ViewHolder，持有每个Item的的所有界面元素
        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView item_tv;
            public ViewHolder(View view){
                super(view);
                item_tv = (TextView)view.findViewById(R.id.item_tv);
            }
        }
    }
}