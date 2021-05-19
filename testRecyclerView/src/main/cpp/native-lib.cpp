#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_testrecyclerview_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_testrecyclerview_MainActivity_myStringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "My String from C++";
    return env->NewStringUTF(hello.c_str());
}