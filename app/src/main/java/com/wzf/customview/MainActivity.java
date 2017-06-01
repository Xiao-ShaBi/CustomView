package com.wzf.customview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.wzf.customview.simpledemo.GridActivity;
import com.wzf.customview.simpledemo.ProgressActivity;
import com.wzf.customview.simpledemo.ToolsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.custom_tools)
    Button customTools;
    @BindView(R.id.custom_progress)
    Button customProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.custom_tools, R.id.custom_progress, R.id.custom_viewgroup})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.custom_tools:
                startActivity(new Intent(this, ToolsActivity.class));
                break;
            case R.id.custom_progress:
                startActivity(new Intent(this, ProgressActivity.class));
                break;
            case R.id.custom_viewgroup:
                startActivity(new Intent(this, GridActivity.class));
                break;
        }
    }
}
