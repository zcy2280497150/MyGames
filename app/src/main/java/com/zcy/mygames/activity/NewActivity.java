package com.zcy.mygames.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.zcy.mygames.R;


/**
 * 跳转用的新的Activity模板(二级Activity)
 * Created by zcy on 2017/9/10.
 */
public class NewActivity extends BaseActivity {

    public static final String FRAGMENT_CLASS_NAME = "FRAGMENT_CLASS_NAME";
    public static final String FRAGMENT_BUNDLE_NAME = "FRAGMENT_BUNDLE_NAME";
    public static final String STATUS_BAR_TXT_BLACK_KEY = "STATUS_BAR_TXT_BLACK_KEY";

    private String fragmentClassName;

    public static <T extends Fragment> void startActivity(Context mContext, Class<T> clazz, Bundle bundle, boolean isBlack) {
        Intent intent = new Intent(mContext, NewActivity.class);
        intent.putExtra(FRAGMENT_CLASS_NAME, clazz.getName());
        intent.putExtra(FRAGMENT_BUNDLE_NAME, bundle);
        intent.putExtra(STATUS_BAR_TXT_BLACK_KEY, isBlack);
        mContext.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTxtBlack(getIntent().getBooleanExtra(STATUS_BAR_TXT_BLACK_KEY, false));
        super.onCreate(savedInstanceState);
        //        setStatusBarTxtBlack(true);
        Intent intent = getIntent();
        initFragment(intent);
    }

    //利用反射来创建对象
    private void initFragment(Intent intent) {
        fragmentClassName = intent.getStringExtra(FRAGMENT_CLASS_NAME);
        try {
            Class<?> forName = Class.forName(fragmentClassName);
            Object object = forName.newInstance();
            if (object instanceof Fragment) {
                Fragment fragment = (Fragment) object;
                Bundle bundle = intent.getBundleExtra(FRAGMENT_BUNDLE_NAME);
                if (null != bundle) {
                    fragment.setArguments(bundle);
                }
                loadFragment(fragment);
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    //加载创建好的Fragment
    private void loadFragment(Fragment fragment) {
        if (null != fragment) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.new_activity_frame_layout, fragment).commit();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_new;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
