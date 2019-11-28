package com.example.memoclass1104;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    private DbAdapter dbAdapter;
    ListView memoList;
    ArrayList<Memo> memos = new ArrayList<>();
    Cursor cursor;
    private ListAdapter dataSimpleAdapter;
    int item_id;
    private AlertDialog dialog = null;
    AlertDialog.Builder builder =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        memoList=findViewById(R.id.memo_List);
        dbAdapter = new DbAdapter(this);
        desplay();
        //更新便條
        memoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.move(position);
                item_id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                intent = new Intent();
                intent.putExtra("item_id",item_id);
                intent.putExtra("type","Edit");
                intent.setClass(MainActivity.this,EditActivity.class);
                startActivity(intent);
            }
        });// memoList結束

        //刪除便條
        memoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.move(position);
                item_id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                dialog = builder.create();
                dialog.show();
                return true;
            }
        });
        //建立dialog訊息窗
        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("訊息")
                .setMessage("確定刪除此便條?")
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    //設定確定按鈕
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        boolean is_deleted = dbAdapter.deleteMemo(item_id);
                        if(is_deleted) {
                            Toast.makeText(MainActivity.this, "已刪除!", Toast.LENGTH_SHORT).show();
                            memos = new ArrayList<>();
                            desplay();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    //設定取消按鈕
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

    } //protected void onCreate 結束

    private void desplay() {
        cursor = dbAdapter.list_mo();
        if(cursor.moveToFirst()){
            do {
                memos.add(new Memo(cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("memo")),
                        cursor.getString(cursor.getColumnIndexOrThrow("date")),
                        cursor.getString(cursor.getColumnIndexOrThrow("bgcolor")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("remind"))));
            }while (cursor.moveToNext());
        }// if 結束
        cursor.moveToFirst();
        dataSimpleAdapter = new ListAdapter(this,memos);
        memoList.setAdapter(dataSimpleAdapter);
    }//desplay結束

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }//onCreateOptionsMenu結束

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                intent = new Intent(MainActivity.this,EditActivity.class);
                intent.putExtra("type","add");
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }//onOptionsItemSelected 結束
}
