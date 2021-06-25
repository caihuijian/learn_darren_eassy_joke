#注释部分解释来自 https://developer.android.com/ndk/guides/android_mk
LOCAL_PATH := $(call my-dir) # 此变量表示源文件在开发树中的位置 my-dir 将返回Android.mk所在当前目录
# CLEAR_VARS 变量，其值由构建系统提供 LEAR_VARS 变量指向一个特殊的 GNU Makefile，后者会为您清除许多 LOCAL_XXX 变量
include $(CLEAR_VARS)
# LOCAL_MODULE 变量存储您要构建的模块的名称 每个模块名称必须唯一，且不含任何空格 如下会生成名为 libnative-lib.so
LOCAL_MODULE    :=bsdiff
# 列举源文件，以空格分隔多个文件
LOCAL_SRC_FILES :=bsdiff.c
# 此变量列出了在构建共享库或可执行文件时使用的额外链接器标记
LOCAL_LDLIBS := -ljnigraphics -llog
# BUILD_SHARED_LIBRARY 变量指向一个 GNU Makefile 脚本，该脚本会收集您自最近 include 以来在 LOCAL_XXX 变量中定义的所有信息。此脚本确定要构建的内容以及构建方式
include $(BUILD_SHARED_LIBRARY)