package com.zcy.mygames.fragment;

import android.view.View;
import android.widget.TextView;

import com.zcy.mygames.R;
import com.zcy.mygames.views.MineSweepingView;

import butterknife.BindView;
import butterknife.OnClick;

public class GameMSFragment extends BaseFragment {

    @BindView(R.id.ms_view) MineSweepingView msView;
    @BindView(R.id.btn_xq) TextView btnXq;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_game_sm;
    }

    @Override
    protected void initViews() {
        btnXq.postDelayed(new Runnable() {
            @Override
            public void run() {
                msView.reStart();
            }
        },2000L);
    }

    @OnClick({R.id.btn_xq,R.id.btn_restart})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_xq:
                boolean selected = btnXq.isSelected();
                btnXq.setSelected(!selected);
                msView.setXq(!selected);
                break;
            case R.id.btn_restart:
                msView.reStart();
                break;
        }
    }
}
