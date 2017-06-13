package com.spartan.karanbir.gallery

import android.app.Application
import android.content.Context

import com.danikula.videocache.HttpProxyCacheServer

/**
 * @author karanbir
 * *
 * @since 5/23/17
 */

class GalleryApp : Application() {
    private var proxy: HttpProxyCacheServer? = null

    private fun newProxy(): HttpProxyCacheServer {
        return HttpProxyCacheServer.Builder(this)
                .maxCacheFilesCount(6)
                .build()
    }

    companion object {

        fun getProxy(context: Context) : HttpProxyCacheServer{
            val app = context.applicationContext as GalleryApp
            return app.proxy ?: app.newProxy()

        }
    }
}
