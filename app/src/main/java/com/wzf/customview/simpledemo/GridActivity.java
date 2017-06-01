package com.wzf.customview.simpledemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;

import com.wzf.customview.R;
import com.wzf.customview.view.GridRadioGroup;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GridActivity extends AppCompatActivity {

    @BindView(R.id.grid_view)
    GridRadioGroup gridView;

    private List<Map<String, Object>> data_list;

    private String[] iconName = {"通讯录", "日历", "照相机", "时钟", "游戏", "短信", "铃声",
            "设置", "语音", "天气", "浏览器", "视频"};
    private SimpleAdapter sim_adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        ButterKnife.bind(this);
        //新建List
        //获取数据
        //新建适配器
        sim_adapter = new SimpleAdapter(this, data_list, R.layout.item, null, null);
        gridView.setAdapter(new AAdapter());
    }

    class AAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 12;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            RadioButton radioButton = new RadioButton(GridActivity.this);
            radioButton.setText(iconName[position]);
            return radioButton;
        }
    }
}
