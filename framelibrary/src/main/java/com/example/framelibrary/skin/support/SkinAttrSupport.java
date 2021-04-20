package com.example.framelibrary.skin.support;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.example.framelibrary.skin.attr.SkinAttr;
import com.example.framelibrary.skin.attr.SkinType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjcai on 2021/4/14.
 */
public class SkinAttrSupport {
    private static final String TAG = "SkinAttrSupport";

    public static List<SkinAttr> getSkinAttrs(Context context, AttributeSet attrs) {
        // 可能的属性 background src textColor textColorHint
        // skinAttrs存储了当前一个view的所有可以换肤的属性
        List<SkinAttr> skinAttrs = new ArrayList<>();
        int attrLength = attrs.getAttributeCount();
        for (int index = 0; index < attrLength; index++) {
            String attrName = attrs.getAttributeName(index);
            String attrValue = attrs.getAttributeValue(index);
            //Log.e(TAG,"attrName -> "+attrName +" ; attrValue -> "+attrValue);
            /*
            <TextView
            android:textColorHint="@color/black"
            android:textColor="@color/half_black"
            android:id="@+id/tv"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Hello World!" />
            以上view的输出
            2021-04-15 20:47:38.708 9628-9628/com.example.learneassyjoke E/SkinAttrSupport: attrName -> textColor ; attrValue -> @2131034442
            2021-04-15 20:47:38.708 9628-9628/com.example.learneassyjoke E/SkinAttrSupport: attrName -> textColorHint ; attrValue -> @2131034263
            2021-04-15 20:47:38.708 9628-9628/com.example.learneassyjoke E/SkinAttrSupport: attrName -> id ; attrValue -> @2131231140
            2021-04-15 20:47:38.708 9628-9628/com.example.learneassyjoke E/SkinAttrSupport: attrName -> layout_width ; attrValue -> -2
            2021-04-15 20:47:38.708 9628-9628/com.example.learneassyjoke E/SkinAttrSupport: attrName -> layout_height ; attrValue -> -2
            2021-04-15 20:47:38.708 9628-9628/com.example.learneassyjoke E/SkinAttrSupport: attrName -> text ; attrValue -> Hello World!

            <ImageView
            android:background="@mipmap/ic_launcher"
            android:src="@drawable/btn_back"
            android:id="@+id/emptyImg"
            android:layout_width="100dp"
            android:layout_height="100dp" />
            以上view的输出
            2021-04-15 20:47:38.809 9628-9628/com.example.learneassyjoke E/SkinAttrSupport: attrName -> id ; attrValue -> @2131230879
            2021-04-15 20:47:38.809 9628-9628/com.example.learneassyjoke E/SkinAttrSupport: attrName -> background ; attrValue -> @2131492864
            2021-04-15 20:47:38.809 9628-9628/com.example.learneassyjoke E/SkinAttrSupport: attrName -> layout_width ; attrValue -> 100.0dip
            2021-04-15 20:47:38.809 9628-9628/com.example.learneassyjoke E/SkinAttrSupport: attrName -> layout_height ; attrValue -> 100.0dip
            2021-04-15 20:47:38.812 9628-9628/com.example.learneassyjoke E/SkinAttrSupport: attrName -> src ; attrValue -> @2131165279

            补充：如果是定义的 #ccc这种写死的值 那么输出为
            textColor ; attrValue -> #ffcccccc
            这种情况是没有办法换肤的 我们会忽略这种情况
            留意观察 我们需要的属性 background src textColor textColorHint 属性的值都是@后面跟一个int值 后面我们会用到这一特点
            */

            // 只获取可能的属性 background src textColor textColorHint
            SkinType skinType = getSkinType(attrName);
            if (skinType != null) {//不为null 就是我们需要换肤的部分
                String resName = getResName(context, attrValue);
                if (TextUtils.isEmpty(resName)) {
                    continue;
                }
                // 创建一个属性
                SkinAttr skinAttr = new SkinAttr(resName, skinType);
                // 存储到集合中
                skinAttrs.add(skinAttr);
            }
        }
        return skinAttrs;
    }

    // 根据资源的id 找出资源的名称 我们希望的资源id应该是类似@2131230879 @2131492864这种的
    private static String getResName(Context context, String attrValue) {
        if (attrValue.startsWith("@")) {//我们只关注开头是@的属性值 其他的我们也做不了换肤
            //去掉开头的@
            attrValue = attrValue.substring(1);
            //转换成int的资源id
            int resId = Integer.parseInt(attrValue);
            //转换为资源名称 如btn_back color_black
            if (resId <= 0) {
                return null;
            }
            return context.getResources().getResourceEntryName(resId);
        }
        return null;
    }

    // 通过名称获取SkinType
    private static SkinType getSkinType(String attrName) {
        SkinType[] skinTypes = SkinType.values();
        // 遍历枚举 如果在枚举里面 则是我们想要的属性 否则返回null
        for (SkinType skinType : skinTypes) {
            if (skinType.getResName().equals(attrName)) {
                // 返回值为 BACKGROUND SRC TEXT_COLOR TEXT_COLOR_HINT等我们定义好的类型
                // Log.e(TAG, "getSkinType: "+skinType );
                return skinType;
            }
        }
        return null;
    }
}
