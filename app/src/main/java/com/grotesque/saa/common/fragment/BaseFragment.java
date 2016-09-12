package com.grotesque.saa.common.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.grotesque.saa.util.FontManager;

import java.io.IOException;

/**
 * Created by 경환 on 2016-04-02.
 */
public abstract class BaseFragment extends Fragment {
    protected Context mContext;
    protected boolean mIsFragmentDestroy = false;
    protected ViewGroup mViewGroupContainer;

    protected abstract int getContentViewResource();

    protected abstract void initOnCreate(Bundle paramBundle);

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        onInitCreated(bundle);
    }

    public void onAttach(Activity paramActivity) {
        super.onAttach(paramActivity);
        this.mContext = paramActivity;
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        initOnCreate(paramBundle);
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        this.mViewGroupContainer = paramViewGroup;
        View view = paramLayoutInflater.inflate(getContentViewResource(), paramViewGroup, false);
        setGlobalFont((ViewGroup)view);
        registerView(view);
        return view;
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.mIsFragmentDestroy = true;
    }

    public void onDetach() {
        super.onDetach();
    }

    protected abstract void onInitCreated(Bundle paramBundle);

    public void onPause() {
        super.onPause();
    }

    public void onResume() {
        super.onResume();
        this.mIsFragmentDestroy = false;
    }

    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
    }

    protected abstract void registerView(View view);


    protected void setGlobalFont(ViewGroup viewgroup) {
        int i = 0;
        while(i < viewgroup.getChildCount())
        {
            View view = viewgroup.getChildAt(i);
            if(view instanceof TextView)
                ((TextView)view).setTypeface(FontManager.getInstance(getActivity()).getTypeface());
            else if(view instanceof Button)
                ((Button) view).setTypeface(FontManager.getInstance(getActivity()).getTypeface());
            else if(view instanceof ViewGroup)
                setGlobalFont((ViewGroup)view);
            i++;
        }
    }
}
