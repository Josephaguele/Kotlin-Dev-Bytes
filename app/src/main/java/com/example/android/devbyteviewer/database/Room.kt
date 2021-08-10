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

package com.example.android.devbyteviewer.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface VideoDao
{

    //Add getVideos() Query function to VideoDao that returns a List of DatabaseVideo:
    //we add LiveData so that UI can observe/ or watch for changes in the database
    //LiveData in Room lets you query from the UI thread and will watch for changes in the database.
    @Query("select * from databasevideo")
    fun getVideos(): LiveData<List<DatabaseVideo>>

    //Add an Insert function called insertAll() to your VideoDao that takes vararg DatabaseVideo:
    //insertAll() is an upsert, so don’t forget to pass it onConflict=OnConflictStrategy.REPLACE!
    //onConflictStrategy.REPLACE means that when a new info is coming from the server, and there is
    // the same info in the database, the new information from the server should replace the info
    // in the database.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg videos: DatabaseVideo)
}