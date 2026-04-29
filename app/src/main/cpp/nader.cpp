#include <jni.h>
#include <string>

// Ini fungsi bawaan Abang sebelumnya (biarkan saja buat tes)
extern "C" JNIEXPORT jstring JNICALL
Java_nademkhan_example_jnistringbridge_MainActivity_getNativeString(JNIEnv* env, jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

// =========================================================
// KABEL BARU: WAJIB DITAMBAH BIAR TOMBOL GAK BIKIN CRASH
// =========================================================

// Kabel untuk tombol StartBigoEngine
extern "C" JNIEXPORT void JNICALL
Java_nademkhan_example_jnistringbridge_MainActivity_StartBigoEngine(JNIEnv* env, jobject thiz, jstring id, jstring ffmpegPath) {
    // Kosongkan dulu untuk tes nyala. 
    // Nanti kode untuk manggil header Go (libbigo_engine.h) kita taruh di dalam sini.
}

// Kabel untuk tombol StopBigoEngine
extern "C" JNIEXPORT void JNICALL
Java_nademkhan_example_jnistringbridge_MainActivity_StopBigoEngine(JNIEnv* env, jobject thiz, jstring id) {
    // Kosongkan dulu untuk tes nyala.
    // Nanti perintah stop Go ditaruh di sini.
}
