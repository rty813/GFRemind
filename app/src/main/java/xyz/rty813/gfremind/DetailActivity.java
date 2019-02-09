package xyz.rty813.gfremind;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {
    private String packageName;
    private MainAdapter adapter;
    private ArrayList<Map<String, String>> list;
    private FloatingActionButton btnSetting;
    private SwipeMenuRecyclerView recyclerView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        btnSetting = findViewById(R.id.btnSetting);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);

        packageName = getIntent().getStringExtra("package");
        toolbar.setTitle(packageName);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        list = MySqliteOperate.queryDetail(packageName);
        adapter = new MainAdapter(this, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keywords = MySqliteOperate.querySetting(packageName);
                View view = getLayoutInflater().inflate(R.layout.popup_setting, null);
                final Switch btnEnable = view.findViewById(R.id.btnEnable);
                final EditText etKeywords = view.findViewById(R.id.etKeywords);
                if (keywords == null) {
                    btnEnable.setChecked(false);
                }
                else {
                    btnEnable.setChecked(true);
                    etKeywords.setVisibility(View.VISIBLE);
                    etKeywords.setText(keywords);
                }
                btnEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            etKeywords.setVisibility(View.VISIBLE);
                        }
                        else {
                            etKeywords.setVisibility(View.INVISIBLE);
                        }
                    }
                });
                new AlertDialog.Builder(DetailActivity.this)
                        .setTitle(packageName)
                        .setView(view)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (!btnEnable.isChecked()){
                                    MySqliteOperate.remove("setting", packageName);
                                } else {
                                    ContentValues values = new ContentValues();
                                    values.put("package", packageName);
                                    values.put("keywords", etKeywords.getText().toString());
                                    MySqliteOperate.insert("setting", values);
                                }
                                MySqliteOperate.keywordsMap = MySqliteOperate.querySetting();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
    }
}
