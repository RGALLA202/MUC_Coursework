package com.rgalla202.weatherdb;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by rgall on 11/11/2016.
 * Separates each row in the recyclerView.
 *Creates a gap/ space between each
 */

public class CustomListGap  extends RecyclerView.ItemDecoration
{
    int Gap;
    public CustomListGap(int Gap)
    {
        this.Gap = Gap;
    }
    @Override
    public  void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        outRect.left=Gap;
        outRect.right=Gap;
        outRect.bottom=Gap;
        if(parent.getChildLayoutPosition(view)==0)
        {
            outRect.top=Gap;
        }
    }
}
