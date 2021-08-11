/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.devbyteviewer.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.devbyteviewer.database.VideosDatabase
import com.example.android.devbyteviewer.database.asDatabaseModel
import com.example.android.devbyteviewer.database.asDomainModel
import com.example.android.devbyteviewer.domain.Video
import com.example.android.devbyteviewer.network.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
/*Repository for fetching devbyte videos from the network and storing them on disk
*
* Repository modules handle data operations. They provide a clean API so that the rest of the app
* can retrieve this data easily. They know where to get the data from and what API alss to make
* when data is updated. You can consider repositories to be mediators between different data
* sources, in our case it mediates between a network API and an offline database cache
* */
class VideosRepository(private val database: VideosDatabase) {

    /**
     * A playlist of videos that can be shown on the screen.
     */
    val videos: LiveData<List<Video>> = Transformations.map(database.videoDao.getVideos()) {
        it.asDomainModel()
    }


    /*Refresh the videos stored in the offline cache.
    *
    * This function uses the IO dispatcher to ensure the database insert database operation
    * happens on the IO dispatcher. By switching to the IO dispatcher using 'withContext' this
    * function is now safe to call from any thread including the Main thread.
    *
    * To actually load the videos for use, observe [videos]
    * Make it a suspend function since it will be called from a coroutine.*/
    suspend fun refreshVideos() {
    /*    Get the data from the network and then put it in the database:
     In refreshVideos(), make a network call to getPlaylist(), and use the await() function
     to tell the coroutine to suspend until the data is available. Then call insertAll()
      to insert the playlist into the database.
      Note the asterisk * is the spread operator. It allows you to pass in an array to a function
       that expects varargs.Run your code on the IO Dispatcher: Inside refreshVideos(), call
       withContext(Dispatchers.IO) to force the Kotlin coroutine to switch to the IO dispatcher.
      */
        withContext(Dispatchers.IO){
            val playlist = Network.devbytes.getPlaylist().await()
            database.videoDao.insertAll(*playlist.asDatabaseModel())
        }


    }
}
