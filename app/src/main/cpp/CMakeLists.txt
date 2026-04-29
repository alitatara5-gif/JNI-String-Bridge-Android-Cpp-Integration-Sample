cmake_minimum_required(VERSION 3.22.1)
project("posix_bridge")

# 1. Buat library jembatan
add_library(native-lib SHARED nader.cpp)

# 2. Cari library log bawaan Android
find_library(log-lib log)

# 3. Kasih tau CMake letak folder mesin Go Abang (Biar dia nggak nyatet alamat server)
target_link_directories(native-lib PRIVATE ${CMAKE_SOURCE_DIR}/../jniLibs/${ANDROID_ABI})

# 4. LAS JADI SATU! (Cukup sebut namanya "bigo_engine", jangan pakai jalur lengkap)
target_link_libraries(native-lib bigo_engine ${log-lib})
