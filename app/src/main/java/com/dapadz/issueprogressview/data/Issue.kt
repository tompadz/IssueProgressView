package com.dapadz.issueprogressview.data

data class Issue(
    val id:Int,
    val label:String,
    val workTime:Long,
    val type : IssueType
)

