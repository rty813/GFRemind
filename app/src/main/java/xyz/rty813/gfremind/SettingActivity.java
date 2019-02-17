package xyz.rty813.gfremind;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    private EditText etExclude;
    private EditText etKeywords;
    private Switch swHideSub;
    private Switch swHideMain;
    private Switch swMain;
    private String packageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        packageName = getIntent().getStringExtra("package");
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(packageName);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        swMain = findViewById(R.id.swMain);
        swHideMain = findViewById(R.id.swHideMain);
        swHideSub = findViewById(R.id.swHideSub);
        etKeywords = findViewById(R.id.etKeywords);
        etExclude = findViewById(R.id.etExclude);
        findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put("enable", swMain.isChecked() ? 1 : 0);
                values.put("keywords", etKeywords.getText().toString());
                values.put("hideMain", swHideMain.isChecked() ? 1 : 0);
                values.put("hideSub", swHideSub.isChecked() ? 1 : 0);
                values.put("exclude", etExclude.getText().toString());
                values.put("package", packageName);
                if (MySqliteOperate.insert("setting", values)) {
                    Toast.makeText(SettingActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                    MySqliteOperate.init(SettingActivity.this);
                    finish();
                } else {
                    Toast.makeText(SettingActivity.this, "修改失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        loadSetting();
    }

    private void loadSetting() {
        SettingModel model = MySqliteOperate.querySetting(packageName);
        if (model != null) {
            swMain.setChecked(model.isEnable());
            swHideMain.setChecked(model.isHideMain());
            swHideSub.setChecked(model.isHideSub());
            etKeywords.setText(model.getKeywords());
            etExclude.setText(model.getExclude());
        }
    }
}
