package com.projectupma.models;

import android.view.View;

public interface OnListItemClick {
    void onClick(View view, int position);
    void onClickEdit();
}
