package com.dapadz.issueprogressview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dapadz.issueprogressview.data.Issue
import com.dapadz.issueprogressview.data.issuesList

class MainViewModel : ViewModel() {

    private var _issues: MutableLiveData<List<Issue>> = MutableLiveData()
    val issues : LiveData<List<Issue>> = _issues

    init {
        getIssues()
    }

    private fun getIssues() {
        _issues.value = issuesList.shuffled()
    }

    fun search(query:CharSequence) {
        if (_issues.value == null) return
        if (query.isNotEmpty()) {
            val queryList = _issues.value !!.filter {
                it.label.contains(query)
            }
            _issues.value = queryList
        }else {
            getIssues()
        }
    }

}