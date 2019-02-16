package xyz.rty813.gfremind;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private ImageView ivStatus;
    private Toolbar toolbar;
    private SwipeMenuRecyclerView recyclerView;
    private ArrayList<Map<String, String>> list;
    private MainAdapter adapter;
    private BroadcastReceiver receiver;
    public static final String ACTION_NOTIFICATION_RECEIVE = "xyz.rty813.MainActivity.action.notification.receive";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.btnRebind:
                        toggleNotificationListenerService();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        NotificationUtils.setChannel(this);
        String string = Settings.Secure.getString(getContentResolver(),"enabled_notification_listeners");
        if (!string.contains(MyNotificationListenerService.class.getName())) {
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        }

        ivStatus = findViewById(R.id.ivStatus);
        new MySqliteOperate(this);
        initRecyclerView();
        initReceiver();
    }

    private void initReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case ACTION_NOTIFICATION_RECEIVE:
                        ContentValues values = intent.getParcelableExtra("values");
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).get("package").equals(values.getAsString("package"))) {
                                list.remove(i);
                                Map<String, String> map = new HashMap<>();
                                map.put("package", values.getAsString("package"));
                                map.put("title", values.getAsString("title"));
                                map.put("content", values.getAsString("content"));
                                map.put("time", values.getAsString("time"));
                                list.add(i, map);
                                adapter.notifyItemChanged(i);
                                return;
                            }
                        }
                        Map<String, String> map = new HashMap<>();
                        map.put("package", values.getAsString("package"));
                        map.put("title", values.getAsString("title"));
                        map.put("content", values.getAsString("content"));
                        map.put("time", values.getAsString("time"));
                        list.add(map);
                        adapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        list = MySqliteOperate.queryNotification();
        adapter = new MainAdapter(this, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setSwipeItemClickListener(new SwipeItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                startActivity(new Intent(MainActivity.this, DetailActivity.class)
                        .putExtra("package", list.get(position).get("package")));
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(ACTION_NOTIFICATION_RECEIVE));
        if (!isNotificationListenerServiceEnabled()) {
            Toast.makeText(this, "Service被关闭！请重新绑定！", Toast.LENGTH_SHORT).show();
        }
        else {
            ivStatus.setImageResource(R.drawable.round_green);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    private void toggleNotificationListenerService() {
        MyNotificationListenerService.reBind(this);
        Toast.makeText(this, "已重新绑定！", Toast.LENGTH_SHORT).show();
    }

    private boolean isNotificationListenerServiceEnabled() {
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(this);
        return packageNames.contains(getPackageName());
    }
}
