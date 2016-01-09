package com.lixicode.lifecycle;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lixicode.library.LifeCycle;
import com.lixicode.library.LifeCycleRetriever;
import com.lixicode.library.LifecycleListener;


/**
 * @author 陈晓辉
 * @description <>
 * @date 16/1/7
 */
public class SecActivity extends FragmentActivity {
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ViewPager pager = (ViewPager) findViewById(R.id.parent);
        pager.setOffscreenPageLimit(1);
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                return new ExampleFragment(String.valueOf(position));
            }

            @Override
            public int getCount() {
                return 5;
            }
        });
        LifeCycleRetriever.with(this).addListener(new LifecycleListener() {
            @Override
            public void onLifeCycleStateChange(@LifeCycle.LifeCycleState int life, int level) {
                Log.e("tag", "ActivityLifeCycle,State:" + haha(life));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("ValidFragment")
    static class ExampleFragment extends android.support.v4.app.Fragment {

        String title;

        ExampleFragment() {
            this.title = "";
        }

        ExampleFragment(String title) {
            this.title = title;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            LifeCycleRetriever.with(this).addListener(new LifecycleListener() {
                @Override
                public void onLifeCycleStateChange(@LifeCycle.LifeCycleState int life, int level) {
                    Log.e("tag", "FragmentLifeCycle:" + title + ",State:" + haha(life));
                }
            });
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            TextView textView = new TextView(getActivity());
            textView.setText(title);
            return textView;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            ExampleApplication.getRefWatcher(getContext()).watch(this);
        }
    }

    static String haha(int life) {
        switch (life) {
            case 0:
                return "init";
            case 1:
                return "onStart";
            case 2:
                return "onStop";
            case 3:
                return "onDestroy";
            case 4:
                return "onTrimMomery";
            case 5:
                return "onLowMomery";
            default:
                return "life" + life;
        }
    }
}
