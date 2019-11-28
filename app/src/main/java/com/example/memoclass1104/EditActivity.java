package com.example.memoclass1104;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txt_title;
    EditText edt_memo;
    Spinner sp_color;
    Button btn_back,btn_ok;
    Intent intent;
    ArrayList<ColorData> color_list = null;
    SpinnerAdapter spinnerAdapter;
    String selected_color; //記錄目前選取的顏色
    private DbAdapter dbAdapter;
    String new_memo,currentTime;
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd  HH:mm"); //設定時間格式
    Bundle bundle;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
            initView();
            dbAdapter = new DbAdapter(this);

            bundle = this.getIntent().getExtras();
            //判斷是否為編輯便條
            if (bundle.getString("type").equals("Edit")){
                txt_title.setText("編輯便條");
                index = bundle.getInt("item_id");
                Cursor cursor = dbAdapter.queryByID(index);
                edt_memo.setText(cursor.getString(cursor.getColumnIndexOrThrow("memo")));

                //尋找目前便條的bgcolor
                for (int i = 0 ; i < spinnerAdapter.getCount(); i++){
                    if (color_list.get(i).code.equals(cursor.getString(cursor.getColumnIndexOrThrow("bgcolor")))){
                        sp_color.setSelection(i);
                        break;
                    }//判斷bgcolor的if結束
                }// for 結束
            } //判斷便條 if 結束
    }

    private void initView(){
        txt_title = findViewById(R.id.txtTitle);
        edt_memo = findViewById(R.id.edtMemo);
        sp_color = findViewById(R.id.sp_Color);
        btn_back = findViewById(R.id.btn_back);
        btn_ok = findViewById(R.id.btn_ok);

        btn_back.setOnClickListener(this);
        btn_ok.setOnClickListener(this);

        // 設定Spinner
        color_list = new ArrayList<ColorData>();
        color_list.add(new ColorData("red","#e4222d"));
        color_list.add(new ColorData("Green","#00c7a4"));
        color_list.add(new ColorData("Blue","#4b7bd8"));
        color_list.add(new ColorData("Orange","#fc8200"));
        color_list.add(new ColorData("Cyan","#18ffff"));
        spinnerAdapter = new SpinnerAdapter(color_list,this);
        sp_color.setAdapter(spinnerAdapter);

        sp_color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_color = color_list.get(position).getCode(); //記錄目前選取顏色
                Log.d("color",color_list.get(position).getCode());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });//sp_color.setOnItemSelectedListeners結束
    } //initView結束

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back :
                intent = new Intent(EditActivity.this,MainActivity.class);
                //intent.setClass(EditActivity.this,MainActivity.class); 可以直接寫在上面
                startActivity(intent);
                finish();
            break;
            case R.id.btn_ok:
                currentTime = df.format(new Date(System.currentTimeMillis()));
                new_memo = edt_memo.getText().toString();
                if (bundle.getString("type").equals("Edit")){
                    //編輯便條
                    try {
                        dbAdapter.UpdateMemo(index,currentTime,new_memo,null,selected_color);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        intent = new Intent(EditActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }else {
                    //新增便條
                    try {
                        dbAdapter.CreateMemo(currentTime,new_memo,null,selected_color);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        intent = new Intent(EditActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            break;
        }// switch結束
    }// onClick結束
}
