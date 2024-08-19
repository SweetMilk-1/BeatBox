package com.example.beatbox

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.SoundPool
import android.util.Log
import java.io.IOException

private const val LOG_TAG = "BeatBox"
private const val SOUNDS_FOLDER = "sample_sounds"
private const val SOUND_POOL_MAX_STREAMS = 1
private const val INITIAL_PLAYBACK_SPEED = 1f

class BeatBox(private val assets: AssetManager) {

    val sounds: List<Sound>
    var playbackSpeed = INITIAL_PLAYBACK_SPEED

    private val soundPool = SoundPool.Builder()
        .setMaxStreams(SOUND_POOL_MAX_STREAMS)
        .build()

    init {
        sounds = loadSounds()
    }

    fun play(sound: Sound) {
        sound.soundId?.let {
            soundPool.play(
                it,
                1f,
                1f,
                1,
                0,
                playbackSpeed
            )
        }
    }

    fun release() {
        soundPool.release()
    }

    //Загружает из AssetManager все звуки и пакует их в объекты класса Sound
    private fun loadSounds(): List<Sound> {
        val soundNames: Array<String>
        val sounds = mutableListOf<Sound>()
        try {
            soundNames = assets.list(SOUNDS_FOLDER)!!
        } catch (ex: Throwable) {
            Log.e(LOG_TAG, "Could not list assets", ex)
            return emptyList()
        }
        soundNames.forEach { soundName ->
            val assetPath = "$SOUNDS_FOLDER/$soundName"
            val sound = Sound(assetPath)
            try {
                load(sound)
                sounds.add(sound)
            } catch (e: IOException) {
                Log.e(LOG_TAG, "Could not load sound $assetPath", e)
            }
        }
        return sounds
    }

    //Загружает отдельный звук при помощи дескриптора файла и сохраняет
    //идентификатор этого звуку в объект Sound
    private fun load(sound: Sound) {
        val afd: AssetFileDescriptor = assets.openFd(sound.assetPath)
        val soundId = soundPool.load(afd, 1)
        sound.soundId = soundId
    }
}