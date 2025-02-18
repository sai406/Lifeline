package com.mstech.lifeline.activities

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.mstech.lifeline.adapter.GalleryAdapter
import com.mstech.lifeline.databinding.ActivityGalleryListBinding
import kotlinx.coroutines.launch
import java.io.File


class FilesListActivity : AppCompatActivity() {
    lateinit var binding: ActivityGalleryListBinding
    private val Folder_Location =
        "/storage/emulated/0/Android/data/com.mstech.lifeline/files/Movies"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.includeTop.ivBack.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        binding.includeTop.tvHeader.setText("Incident Videos")
        binding.recyclerView.layoutManager = GridLayoutManager(
            this,
            2
        )
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        lifecycleScope.launch {
            val f = File(Folder_Location)

            val fil = f.listFiles()
            if (fil!= null && fil.size > 0) {
                binding.recyclerView.adapter = GalleryAdapter(this@FilesListActivity, fil)

            } else {
                ToastUtils.showShort("No Files Found")
            }
        }


    }
}