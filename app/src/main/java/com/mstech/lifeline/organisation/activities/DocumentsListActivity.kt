package com.mstech.lifeline.organisation.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.mstech.lifeline.activities.BaseActivity
import com.mstech.lifeline.databinding.ActivityDocumentsListBinding
import com.mstech.lifeline.organisation.adapter.DocumentsAdapter
import com.mstech.lifeline.organisation.adapter.LinksAdapter
import com.mstech.lifeline.organisation.model.DocumentsItem
import com.mstech.lifeline.organisation.model.LinksItem
import com.mstech.lifeline.organisation.model.RetroApi


import kotlinx.coroutines.launch

class DocumentsListActivity : BaseActivity() {
    lateinit var binding : ActivityDocumentsListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocumentsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.links.layoutManager = LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        )
        binding.documents.layoutManager = LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        )
        binding.links.setNestedScrollingEnabled(false);
        binding.documents.setNestedScrollingEnabled(false);
        lifecycleScope.launch {
            getDocumentList()
        }
    }
    private suspend fun getDocumentList() {
        showPDialog("Please wait ..")
        val response = RetroApi().getDocuments(getIntent().extras?.getString("articleId")!!)
        if (response.isSuccessful) {
            binding.links.adapter = response.body()?.links.let {
                LinksAdapter(
                        this,
                        it as List<LinksItem>
                )
            }
            binding.documents.adapter = response.body()?.documents.let {
                DocumentsAdapter(
                        this,
                        it as List<DocumentsItem>
                )
            }
        } else {
            ToastUtils.showShort(response.errorBody()?.string())
        }
        hidePDialog()
    }
}