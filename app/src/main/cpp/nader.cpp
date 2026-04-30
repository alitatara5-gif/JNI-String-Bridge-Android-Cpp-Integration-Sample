#include <jni.h>
#include <string>

// Deklarasi fungsi dari libbigo_engine.so (Go)
extern "C" {
    extern void StartBigoEngine(char* id, char* ffmpegPath);
    extern void StopBigoEngine(char* id);
}

// 1. Jika getNativeString masih ada di MainActivity.java, biarkan ini. 
// Jika sudah dihapus dari Java, hapus juga bagian ini.
extern "C" JNIEXPORT jstring JNICALL
Java_nademkhan_example_jnistringbridge_MainActivity_getNativeString(JNIEnv* env, jobject /* this */) {
    std::string hello = "Hello from C++ with Go Engine Ready";
    return env->NewStringUTF(hello.c_str());
}

// =========================================================
// PENTING: Nama fungsi di bawah ini diganti dari MainActivity jadi RecorderService
// =========================================================

// Kabel untuk StartBigoEngine
extern "C" JNIEXPORT void JNICALL
Java_nademkhan_example_jnistringbridge_RecorderService_StartBigoEngine(JNIEnv* env, jobject thiz, jstring id, jstring ffmpegPath) {
    
    // 1. Ubah jstring (Java) jadi char* (C/Go)
    const char *nativeId = env->GetStringUTFChars(id, 0);
    const char *nativePath = env->GetStringUTFChars(ffmpegPath, 0);

    // 2. PANGGIL MESIN GO!
    StartBigoEngine((char*)nativeId, (char*)nativePath);

    // 3. Lepas memori biar HP gak berat
    env->ReleaseStringUTFChars(id, nativeId);
    env->ReleaseStringUTFChars(ffmpegPath, nativePath);
}

// Kabel untuk StopBigoEngine
extern "C" JNIEXPORT void JNICALL
Java_nademkhan_example_jnistringbridge_RecorderService_StopBigoEngine(JNIEnv* env, jobject thiz, jstring id) {
    
    // 1. Ubah jstring (Java) jadi char* (C/Go)
    const char *nativeId = env->GetStringUTFChars(id, 0);

    // 2. MATIKAN MESIN GO!
    StopBigoEngine((char*)nativeId);

    // 3. Lepas memori
    env->ReleaseStringUTFChars(id, nativeId);
}
