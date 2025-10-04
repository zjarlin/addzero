
// The code in this file is a convention plugin - a Gradle mechanism for sharing reusable build logic.
// `buildSrc` is a Gradle-recognized directory and every plugin there will be easily available in the rest of the build.

plugins {
    id("org.graalvm.buildtools.native")
}

//graalvmNative {
//    binaries {
//        named("main") {
//            // 优化可执行文件大小
//            buildArgs.add("-Os")
//
//            // 禁用回退模式以确保完全的AOT编译
//            buildArgs.add("--no-fallback")
//
//            // 在构建时初始化Kotlin相关类以减小运行时大小
//            buildArgs.add("--initialize-at-build-time=kotlin")
//
//            // 运行时初始化一些特定类
//            buildArgs.add("--initialize-at-run-time=kotlinx.coroutines.internal.LockFreeTaskQueueCore,kotlinx.coroutines.scheduling.CoroutineScheduler,kotlin.uuid.SecureRandomHolder")
//        }
//    }
//
//    // 禁用工具链检测以避免问题
//    toolchainDetection.set(false)
//}

