cmake_minimum_required(VERSION 3.22.1)
project("posix_bridge")

# 1. Daftarkan Library Go Abang
add_library(bigo_engine SHARED IMPORTED)
set_target_properties(bigo_engine PROPERTIES IMPORTED_LOCATION
    ${CMAKE_SOURCE_DIR}/../jniLibs/${ANDROID_ABI}/libbigo_engine.so)

# 2. Buat library jembatan native-lib
add_library(native-lib SHARED native-lib.cpp)

# 3. Cari library log bawaan Android
find_library(log-lib log)

# 4. LAS JADI SATU!
target_link_libraries(native-lib bigo_engine ${log-lib})
