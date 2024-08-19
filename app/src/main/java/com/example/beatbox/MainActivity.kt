package com.example.beatbox

import android.os.Bundle
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.beatbox.databinding.ActivityMainBinding
import com.example.beatbox.databinding.SoundListItemBinding

class MainActivity : AppCompatActivity() {
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel.beatBox = BeatBox(assets)
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, SPAN_COUNT)
            adapter = SoundAdapter(viewModel.beatBox.sounds)
        }

        binding.playbackSpeedSeekBar.apply {
            val initialPlaybackSpeedValue = Math.round(viewModel.beatBox.playbackSpeed * 100)
            progress = initialPlaybackSpeedValue
            binding.playbackSpeedValue.text =
                getString(R.string.playback_speed_text, initialPlaybackSpeedValue)

            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(sb: SeekBar?, value: Int, animation: Boolean) {
                    viewModel.beatBox.playbackSpeed = value * 1f / 100
                    binding.playbackSpeedValue.text = getString(R.string.playback_speed_text, value)
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {}

                override fun onStopTrackingTouch(p0: SeekBar?) {}
            })
        }

    }

    companion object {
        const val SPAN_COUNT = 3
    }

    inner class SoundViewHolder(private val binding: SoundListItemBinding) :
        ViewHolder(binding.root) {
        init {
            binding.viewModel = SoundViewModel(viewModel.beatBox)
        }

        fun bind(sound: Sound) {
            binding.apply {
                viewModel?.sound = sound
            }
        }
    }

    inner class SoundAdapter(
        private val soundList: List<Sound>
    ) : Adapter<SoundViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundViewHolder {

            //DataBindingUtil provide more methods for create data binding objects
            val binding = DataBindingUtil.inflate<SoundListItemBinding>(
                layoutInflater, //inflater
                R.layout.sound_list_item, //layout id
                parent, //parent view
                false // attach to parent
            )

            binding.lifecycleOwner = this@MainActivity

            return SoundViewHolder(binding)
        }


        override fun getItemCount(): Int = soundList.size

        override fun onBindViewHolder(holder: SoundViewHolder, position: Int) {
            val sound = soundList[position]
            holder.bind(sound)
        }
    }
}