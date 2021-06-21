//
// Created by hjcai on 2021/6/18.
//
#include "com_example_testrecyclerview_MainActivity.h"


JNIEXPORT jstring

JNICALL Java_com_example_testrecyclerview_MainActivity_testCString
        (JNIEnv *env, jobject obj) {
    return (*env)->NewStringUTF(env, "Hello, I'm from jni");
}
