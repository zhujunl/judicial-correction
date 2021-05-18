package com.miaxis.enroll;

public interface OnClickItemListener {

    void    onClickItemListener(int position,boolean isNext,String title,String coding);

    void    onClickItemChildListener(int position,String title,String coding);
}
