#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_testrecyclerview_MainActivity_testString1(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "testString1 C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_testrecyclerview_MainActivity_testString2(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "testString2 C++";
    return env->NewStringUTF(hello.c_str());
}