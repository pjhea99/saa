// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.grotesque.saa.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.grotesque.saa.R;
import com.grotesque.saa.common.fragment.ActionBarFrameLayout;

public class HomeFragmentLayout extends ActionBarFrameLayout
{

    private float b;
    private float c;
    private float d;
    private float e;
    private FrameLayout f;
    private RecyclerView g;

    public HomeFragmentLayout(Context context)
    {
        super(context);
    }

    public HomeFragmentLayout(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);

    }

    public HomeFragmentLayout(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);

    }

    private void getRect()
    {
        if(f != null)
        {
            b = f.getX();
            c = b + (float)f.getWidth();
            d = f.getY();
            e = d + (float)f.getHeight();
        }
    }

    public boolean dispatchTouchEvent(MotionEvent motionevent)
    {
        float f1 = motionevent.getX();
        float f2 = motionevent.getY();
        if(f != null)
        {
            if(b == 0.0F && c == 0.0F && d == 0.0F && e == 0.0F)
                getRect();
            if(f1 > b && f1 < c && f2 > d && f2 < e)
            {
                if(f != null && g != null && g.getVisibility() == VISIBLE)
                {
                    f.dispatchTouchEvent(motionevent);
                    return true;
                }
            }
        }
        return super.dispatchTouchEvent(motionevent);
    }

    protected void onFinishInflate()
    {
        super.onFinishInflate();
        f = (FrameLayout)findViewById(R.id.wrapper_header);
        if(f != null)
            g = (RecyclerView)f.findViewById(R.id.today_keyword_list);
    }

    public boolean onTouchEvent(MotionEvent motionevent)
    {
        int i = motionevent.getAction();
        return super.onTouchEvent(motionevent);
    }


}
