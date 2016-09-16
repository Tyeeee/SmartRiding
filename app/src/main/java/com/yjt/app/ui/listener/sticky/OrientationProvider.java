package com.yjt.app.ui.listener.sticky;

import android.support.v7.widget.RecyclerView;

public interface OrientationProvider {

    int getOrientation(RecyclerView recyclerView);

    boolean isReverseLayout(RecyclerView recyclerView);
}
