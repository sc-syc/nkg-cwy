package cn.les;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

import cn.les.base.BaseFragment;
import cn.les.map.MapFragment;
import cn.les.me.MeFragment;
import cn.les.news.NewsFragment;
import cn.les.task.TaskFragment;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;

public class MainActivity extends AppCompatActivity {

    private int last;
    private final int[] buttonIds = new int[]{R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4};
    private final List<BaseFragment> fragments = new ArrayList<>(buttonIds.length);
    public final ViewGroup[] buttons = new ViewGroup[buttonIds.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        changeFragment();


        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }

    private void initViews() {
        fragments.add(new MapFragment());
        fragments.add(new TaskFragment());
        fragments.add(new NewsFragment());
        fragments.add(new MeFragment());
        for (int i = 0; i < buttons.length; i++) {
            final int j = i;
            buttons[i] = findViewById(buttonIds[i]);
            View.OnClickListener clickListener = arg0 -> {
                if (buttons[j].isSelected()) {
                    return;
                }
                showElement(j);
            };
            buttons[i].setOnClickListener(clickListener);
        }
    }

    private void showElement(int index) {
        for (ViewGroup button : buttons) {
            button.setSelected(false);
        }
        buttons[index].setSelected(true);
        try {
            if (last != index) {
                switchFragment(last, index);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void changeFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.tab_main_content, fragments.get(0)).commit();
        getSupportFragmentManager().executePendingTransactions();
        buttons[0].setSelected(true);
        last = 0;
    }

    private void switchFragment(int from, int to) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!fragments.get(to).isAdded()) {
            transaction.hide(fragments.get(from)).add(R.id.tab_main_content, fragments.get(to)).commitAllowingStateLoss();
        } else {
            transaction.hide(fragments.get(from)).show(fragments.get(to)).commitAllowingStateLoss();
        }
        if (from != to) {
            fragments.get(from).setActive(false);
        }
        fragments.get(to).setActive(true);
        last = to;
        getSupportFragmentManager().executePendingTransactions();
    }
}