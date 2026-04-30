#include <jni.h>
#include <string>

// Deklarasi fungsi dari mesin Go (libbigo_engine.so)
extern "C" {
    extern void StartBigoEngine(char* id, char* ffmpegPath);
    extern void StopBigoEngine(char* id);
}

// --- KABEL START ---
// Perhatikan: Nama kelas di tengah harus "RecorderService"
extern "C" JNIEXPORT void JNICALL
Java_nademkhan_example_jnistringbridge_RecorderService_StartBigoEngine(JNIEnv* env, jobject thiz, jstring id, jstring ffmpegPath) {
    
    const char *nativeId = env->GetStringUTFChars(id, 0);
    const char *nativePath = env->GetStringUTFChars(ffmpegPath, 0);

    // Panggil mesin Go
    StartBigoEngine((char*)nativeId, (char*)nativePath);

    env->ReleaseStringUTFChars(id, nativeId);
    env->ReleaseStringUTFChars(ffmpegPath, nativePath);
}

// --- KABEL STOP ---
extern "C" JNIEXPORT void JNICALL
Java_nademkhan_example_jnistringbridge_RecorderService_StopBigoEngine(JNIEnv* env, jobject thiz, jstring id) {
    
    const char *nativeId = env->GetStringUTFChars(id, 0);

    // Stop mesin Go
    StopBigoEngine((char*)nativeId);

    env->ReleaseStringUTFChars(id, nativeId);
}
