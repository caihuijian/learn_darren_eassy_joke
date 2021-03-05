package com.example.navigationbar;

/**
 * Created by hjcai on 2021/3/4.
 * 定义导航条规范
 */
interface INavigationBar {
    // 绑定布局ID
    int bindLayoutId();

    // 给View设置参数
    void applyView();
}
