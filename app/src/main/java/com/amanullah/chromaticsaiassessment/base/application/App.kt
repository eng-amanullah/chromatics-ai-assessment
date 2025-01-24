package com.amanullah.chromaticsaiassessment.base.application

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.util.DebugLogger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application(), ImageLoaderFactory {
    // For Image Caching
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(context = this)
            .memoryCache {
                MemoryCache.Builder(context = this)
                    .maxSizePercent(percent = 0.20)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve(relative = "image_cache"))
                    .maxSizeBytes(size = 5 * 1024 * 1024)
                    .build()
            }
            .logger(DebugLogger())
            .respectCacheHeaders(enable = false)
            .build()
    }
}