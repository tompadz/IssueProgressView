package com.dapadz.issueprogressview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.dapadz.issueprogressview.databinding.ActivityMainBinding
import com.dapadz.issueprogressview.views.SearchableToolbarListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val viewModel : MainViewModel by viewModels()
    private val adapter = IssuesAdapter()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        initRecyclerView()
        observeViewModel()
    }

    private fun initToolbar() {
        with(binding) {
            toolbar.addSearchableToolbarListener(object : SearchableToolbarListener {
                override fun onSearchEnableChange(isEnable : Boolean) = appbar.setExpanded(!isEnable)
                override fun onQueryChange(query : CharSequence) = updateQuery(query)
            })
        }
    }

    private fun initRecyclerView() {
        with(binding) {
            recyclerView.adapter = adapter
            recyclerView.addItemDecoration(DividerDecorator())
        }
    }

    private fun observeViewModel() {
        viewModel.issues.observe(this) {
            adapter.setData(it)
            with(binding) {
                if (progressBar.itemsCount == 0) {
                    progressBar.setIssues(it)
                }else {
                    progressBar.showCurrentIssues(it)
                }
            }
        }
    }

    private fun updateQuery(query:CharSequence) {
        viewModel.search(query)
    }
}