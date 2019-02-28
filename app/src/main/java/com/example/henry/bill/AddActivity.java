package com.example.henry.bill;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private String type ;
    private int ID;
    private int select_year;
    private int select_month;
    private int select_day;
    private String pay_or_get;
    private String cost;
    Button button2;
    Button button;
    TextView sort_text;
    TextView edit_view;
    TextView date_textView;
    TextView pay_option;
    TextView get_option;
    MyAdapter adapter;
    private DatePickerDialog dpd;
    Biller biller;
    private List<Sort> sortList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        LitePal.getDatabase();/** 建立数据库 **/
        pay_or_get = "pay";//预设为支付
        Intent intent = getIntent();
        ID = Integer.parseInt(intent.getStringExtra("ID"));
        date_textView = findViewById(R.id.date_text);
        button = findViewById(R.id.data_selector);
        sort_text = findViewById(R.id.sort_text);
        button2 = findViewById(R.id.button_2);
        edit_view = findViewById(R.id.edit_text);
        pay_option=findViewById(R.id.pay_option);
        get_option=findViewById(R.id.get_option);
        /** 对输入价格的输入框进行限制  **/
        edit_view.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        edit_view.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable edt)
            {
                String temp = edt.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2)
                {
                    edt.delete(posDot + 3, posDot + 4);
                }
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
        });
        /** 打开时间选择器 **/
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                if (dpd == null) {
                    dpd = DatePickerDialog.newInstance(
                            AddActivity.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                } else {
                    dpd.initialize(
                            AddActivity.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                }
                dpd.setAccentColor("#2F4F4F");
                dpd.show(getFragmentManager(),"Datepickerdialog");
            }
        });
        /** 设置recyclerview **/
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(5,StaggeredGridLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyAdapter(sortList);
        Dosomething();
        recyclerView.setAdapter(adapter);
        /**设置recyclerview的点击事件 **/
        adapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Sort sort = sortList.get(position);
                type = sort.getName();
                sort_text.setText(sort.getName());
                //Toast.makeText(view.getContext(),"you clicker"+sort.getName(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
        pay_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payInitSorts();
                adapter.notifyDataSetChanged();
                Resources resources=getBaseContext().getResources();
                Drawable drawable=resources.getDrawable(R.color.colorPrimary);
                get_option.setBackground(drawable);
                get_option.setTextColor(Color.WHITE);
                Resources resources_1 = getBaseContext().getResources();
                Drawable drawable1 = resources_1.getDrawable(R.color.write);
                pay_option.setBackground(drawable1);
                pay_option.setTextColor(Color.BLACK);
                type = new String("账单类型");
                sort_text.setText(type);
                pay_or_get = "pay";
            }
        });
        get_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomeInitSorts();
                adapter.notifyDataSetChanged();
                Resources resources=getBaseContext().getResources();
                Drawable drawable=resources.getDrawable(R.color.write);
                get_option.setBackground(drawable);
                get_option.setTextColor(Color.BLACK);
                Resources resources2=getBaseContext().getResources();
                Drawable drawable2=resources.getDrawable(R.color.colorPrimary);
                pay_option.setBackground(drawable2);
                pay_option.setTextColor(Color.WHITE);
                type = new String("账单类型");
                sort_text.setText(type);
                pay_or_get = "get";
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(edit_view.getText())){
                    Toast.makeText(AddActivity.this,"请输入金额",Toast.LENGTH_SHORT).show();
                }else if (select_year == 0){
                    Toast.makeText(AddActivity.this,"请选择时间",Toast.LENGTH_SHORT).show();
                }
                else if (type.equals("账单类型")){
                    Toast.makeText(AddActivity.this,"请选择种类",Toast.LENGTH_SHORT).show();
                }else {
                    if (ID==-1){
                        Toast.makeText(AddActivity.this,"成功入库",Toast.LENGTH_SHORT).show();
                        cost =new String(edit_view.getText().toString());
                        Add_data(pay_or_get,type,select_year,select_month,select_day,cost);
                        AddActivity.this.finish();
                    }else {
                        Toast.makeText(AddActivity.this,"成功编辑",Toast.LENGTH_SHORT).show();
                        cost =new String(edit_view.getText().toString());
                        Update(pay_or_get,type,select_year,select_month,select_day,cost);
                        AddActivity.this.finish();
                    }
                }
            }
        });
    }
    @Override
    /** 时间选择器 **/
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String select_date = " 时间: "+dayOfMonth+"/"+(++monthOfYear)+"/"+year;
        select_year = year;
        select_month = monthOfYear;
        select_day = dayOfMonth;
        date_textView.setText(select_date);
    }
    private void payInitSorts(){
        sortList.clear();
        Sort sort = new Sort("Food",R.mipmap.ty_food);
        sortList.add(sort);
        Sort sort1 = new Sort("House",R.mipmap.ty_house);
        sortList.add(sort1);
        Sort sort2 = new Sort("Clothes",R.mipmap.ty_clothes);
        sortList.add(sort2);
        Sort sort3 = new Sort("Kids",R.mipmap.ty_kid);
        sortList.add(sort3);
        Sort sort4 = new Sort("Medical",R.mipmap.ty_medical);
        sortList.add(sort4);
        Sort sort5 = new Sort("Amuse",R.mipmap.ty_amusement);
        sortList.add(sort5);
        Sort sort6 = new Sort("Shopping",R.mipmap.ty_shoping);
        sortList.add(sort6);
    }
    private void  incomeInitSorts(){
        sortList.clear();
        Sort sort1 = new Sort("Wages",R.mipmap.ty_wages);
        sortList.add(sort1);
        Sort sort2 = new Sort("Gift",R.mipmap.ty_hongbao);
        sortList.add(sort2);
        Sort sort3 = new Sort("Financing",R.mipmap.ty_financing);
        sortList.add(sort3);
    }
    private void Add_data(String pay_or_get,String type,int select_year,int select_month,int select_day,String cost){
        Biller biller = new Biller();
        biller.setPay_or_get(pay_or_get);
        biller.setSort(type);
        biller.setYear(select_year);
        biller.setMonth(select_month);
        biller.setDay(select_day);
        biller.setCost(cost);
        biller.save();
    }
    private void Update(String pay_or_get,String type,int select_year,int select_month,int select_day,String cost){
        Biller biller = new Biller();
        biller.setPay_or_get(pay_or_get);
        biller.setSort(type);
        biller.setYear(select_year);
        biller.setMonth(select_month);
        biller.setDay(select_day);
        biller.setCost(cost);
        biller.update(ID);
    }
    private void Dosomething(){
        if (ID==-1){
            initedit();
            payInitSorts();
            Resources resources_1 = getBaseContext().getResources();
            Drawable drawable1 = resources_1.getDrawable(R.color.write);
            pay_option.setBackground(drawable1);
            pay_option.setTextColor(Color.BLACK);
        }else {
            biller = DataSupport.find(Biller.class,ID);
            select_year = biller.getYear();
            select_month = biller.getMonth();
            select_day = biller.getDay();
            cost = biller.getCost();
            type = biller.getSort();
            pay_or_get = biller.getPay_or_get();
            sort_text.setText(type);
            String select_date = " 时间: "+select_day+"/"+select_month+"/"+select_year;
            edit_view.setText(cost);
            date_textView.setText(select_date);
            if (pay_or_get.equals("pay")){
                payInitSorts();
                Resources resources_1 = getBaseContext().getResources();
                Drawable drawable1 = resources_1.getDrawable(R.color.write);
                pay_option.setBackground(drawable1);
                pay_option.setTextColor(Color.BLACK);
            }else {
                incomeInitSorts();
                Resources resources_1 = getBaseContext().getResources();
                Drawable drawable1 = resources_1.getDrawable(R.color.write);
                get_option.setBackground(drawable1);
                get_option.setTextColor(Color.BLACK);
            }
        }
    }
    private void initedit(){
        cost =new String("00.00");
        select_year=0;
        select_month=0;
        select_day=0;
        type = new String("账单类型");
    }
}
