package com.zcy.mygames;

import android.os.Bundle;
import android.view.View;

import com.zcy.mygames.activity.BaseActivity;
import com.zcy.mygames.activity.NewActivity;
import com.zcy.mygames.fragment.GameLTZJFragment;
import com.zcy.mygames.fragment.GameMSFragment;

import butterknife.OnClick;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.btn_ms,R.id.btn_ltzj})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ms:
                NewActivity.startActivity(this , GameMSFragment.class , null,false);
                break;
            case R.id.btn_ltzj:
                NewActivity.startActivity(this , GameLTZJFragment.class , null,false);
                break;
        }
    }
}
