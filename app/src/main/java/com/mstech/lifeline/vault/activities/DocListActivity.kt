package com.mstech.lifeline.vault.activities

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.mstech.lifeline.databinding.ActivityFileslistBinding
import com.mstech.lifeline.vault.adapter.DocAdapter
import kotlinx.coroutines.launch
import java.io.File

class DocListActivity : AppCompatActivity() {
    lateinit var binding: ActivityFileslistBinding
    private val imageLocation =
        "/storage/emulated/0/Android/data/com.mstech.lifeline/files/Pictures"
    private val documentlocation =
        "/storage/emulated/0/Android/data/com.mstech.lifeline/files/Documents"
    var photos = true
    var docs = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFileslistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.includeTop.ivBack.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        binding.includeTop.tvHeader.setText("Add Files")
        binding.imageRecyclerview.layoutManager = GridLayoutManager(
            this,
            3
        )
        binding.docRecyclerview.layoutManager = GridLayoutManager(
            this,
            3
        )
        binding.adddoc.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, AddFileActivity::class.java))
            finish()
        })
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            val f = File(imageLocation)

            val fil = f.listFiles()
            if (fil != null && fil.isNotEmpty()) {
                var adapter = DocAdapter(this@DocListActivity, fil, "1", this@DocListActivity)
                binding.imageRecyclerview.adapter = adapter
            } else {
                binding.photoslayout.visibility = View.GONE
                photos = false
                ToastUtils.showShort("No Photos Found")
            }
        }

        lifecycleScope.launch {
            val f = File(documentlocation)

            val fil = f.listFiles()
            if (fil != null && fil.isNotEmpty()) {
                var adapter = DocAdapter(this@DocListActivity, fil, "2", this@DocListActivity)
                binding.docRecyclerview.adapter = adapter
                adapter.notifyDataSetChanged()
            } else {
                binding.doclayout.visibility = View.GONE
                docs = false
                ToastUtils.showShort("No Documents Found")
            }
        }

        if (!photos && !docs) {
            binding.addtext.visibility = View.VISIBLE
        } else {
            binding.addtext.visibility = View.GONE
        }
    }

    fun refresh() {
        onResume()
    }
}