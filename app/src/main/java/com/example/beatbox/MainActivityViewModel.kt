package com.example.beatbox


import android.content.res.AssetManager
import androidx.lifecycle.ViewModel

class MainActivityViewModel() : ViewModel() {
    lateinit var beatBox: BeatBox

    override fun onCleared() {
        super.onCleared()
        beatBox.release()
    }
}