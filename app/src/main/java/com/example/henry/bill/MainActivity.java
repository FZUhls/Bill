package com.example.henry.bill;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import java.util.ArrayList;
import java.util.List;

import static org.litepal.crud.DataSupport.delete;

// TODO: 2019/3/1  根据月 日 改变RecyclerView 还没完成
public class MainActivity extends AppCompatActivity {
    private List<Biller> billerList = new ArrayList<>();
    private DrawerLayout drawerLayout;
    private FloatingActionButton floatingActionButton;
    MyAdapter_2 adapter_2;
    RecyclerView recyclerView;
    private TextView allpay;
    private TextView allincome;
    private TextView Total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        allpay = findViewById(R.id.pay_count);
        allincome = findViewById(R.id.income_count);
        Total = findViewById(R.id.total);
        NavigationView navView = findViewById (R.id.nav_view);
        floatingActionButton = findViewById(R.id.add_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = -1;
                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                intent.putExtra("ID",i+"");
                startActivity(intent);
            }
        });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ui_menu2);
        }
        billerList = Allbills();
        recyclerView = findViewById(R.id.bill_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter_2 = new MyAdapter_2(billerList);
        recyclerView.setAdapter(adapter_2);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                adapter_2.notifyDataSetChanged();
                drawerLayout.closeDrawers();
                return true;
            }
        });
        adapter_2.setOnItemClickListener(new MyAdapter_2.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Biller biller =billerList.get(position);
                Toast.makeText(MainActivity.this,"This bill cost+"+biller.getCost()+" yuan",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onItemLongClick(View view, int position) {
                showPopupMenu(view,position);
            }
        });
        Button button_3 = findViewById(R.id.button_3);
        button_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataSupport.deleteAll(Biller.class);
                billerList.clear();
                adapter_2.notifyDataSetChanged();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.chose_date:

                break;
                default:
        }
        return true;
    }
    private List<Biller> Allbills(){
        List<Biller> billerList_2 = DataSupport.findAll(Biller.class);
        return billerList_2;
    }

    // TODO: 2019/3/1 统计功能待完成
    private double AllPay(){
        List<Biller> Pay_billerList = DataSupport.where("pay_or_get = ?","pay").find(Biller.class);
        double Total = 0;
        for(int i=0;i<Pay_billerList.size();i++){
            Pay_billerList.get(i).getCost();
        }
        return Total;
    }
    private void showPopupMenu(View view,int position){
        PopupMenu popupMenu = new PopupMenu(MainActivity.this,view);
        popupMenu.getMenuInflater().inflate(R.menu.popupmenu,popupMenu.getMenu());
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int ID;
                switch (item.getItemId()){
                    case R.id.delete_it :
                        Biller biller_1 = billerList.get(position);
                        DataSupport.delete(Biller.class,biller_1.getId());
                        billerList.remove(position);
                        adapter_2.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this,"You Chose Delete",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.edit_it:
                        Biller biller = billerList.get(position);
                        ID=biller.getId();
                        Intent intent = new Intent(MainActivity.this,AddActivity.class);
                        intent.putExtra("ID",ID+"");
                        startActivity(intent);
                        break;
                    default:
                         break;
                }
                return true;
            }
        });
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {

            }
        });

    }
    /**更新链表数据时
     *
     *
     * 千万不能直接用等号赋值
     *
     *
     * 这样相当于直接改变list的地址**/
    @Override
    protected void onRestart() {
        super.onRestart();
        billerList.clear();
        List<Biller> billerList_2 = Allbills();
        for(int i=0;i<billerList_2.size();i++){
            billerList.add(billerList_2.get(i));
        }
        adapter_2.notifyDataSetChanged();
        Log.d("TAG","onRestart");
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.chose_date,menu);
        return true;
    }
}
