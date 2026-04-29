#include <jni.h>
#include <string>

// Deklarasi fungsi yang ada di dalam libbigo_engine.so (Go)
// Kita kasih tahu C++ kalau fungsi ini bakal ada pas aplikasi jalan
extern "C" {
    extern void StartBigoEngine(char* id, char* ffmpegPath);
    extern void StopBigoEngine(char* id);
}

extern "C" JNIEXPORT jstring JNICALL
Java_nademkhan_example_jnistringbridge_MainActivity_getNativeString(JNIEnv* env, jobject /* this */) {
    std::string hello = "Hello from C++ with Go Engine Ready";
    return env->NewStringUTF(hello.c_str());
}

// =========================================================
// KABEL AKTIF: SEKARANG SUDAH NYAMBUNG KE GO
// =========================================================

// Kabel untuk tombol StartBigoEngine
extern "C" JNIEXPORT void JNICALL
Java_nademkhan_example_jnistringbridge_MainActivity_StartBigoEngine(JNIEnv* env, jobject thiz, jstring id, jstring ffmpegPath) {
    
    // 1. Ubah jstring (Java) jadi char* (C/Go)
    const char *nativeId = env->GetStringUTFChars(id, 0);
    const char *nativePath = env->GetStringUTFChars(ffmpegPath, 0);

    // 2. PANGGIL MESIN GO!
    StartBigoEngine((char*)nativeId, (char*)nativePath);

    // 3. Lepas memori biar HP gak berat
    env->ReleaseStringUTFChars(id, nativeId);
    env->ReleaseStringUTFChars(ffmpegPath, nativePath);
}

// Kabel untuk tombol StopBigoEngine
extern "C" JNIEXPORT void JNICALL
Java_nademkhan_example_jnistringbridge_MainActivity_StopBigoEngine(JNIEnv* env, jobject thiz, jstring id) {
    
    // 1. Ubah jstring (Java) jadi char* (C/Go)
    const char *nativeId = env->GetStringUTFChars(id, 0);

    // 2. MATIKAN MESIN GO!
    StopBigoEngine((char*)nativeId);

    // 3. Lepas memori
    env->ReleaseStringUTFChars(id, nativeId);
}
