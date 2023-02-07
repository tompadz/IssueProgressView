package com.dapadz.issueprogressview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dapadz.issueprogressview.AndroidUtils.Companion.dpf
import com.dapadz.issueprogressview.AndroidUtils.Companion.setCornerRadiusOfView
import com.dapadz.issueprogressview.data.Issue
import com.dapadz.issueprogressview.databinding.ItemIssueBinding


class IssuesAdapter : RecyclerView.Adapter<IssuesAdapter.IssueViewHolder>() {

    private var issues = mutableListOf<Issue>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data:List<Issue>) {
        with(issues) {
            clear()
            addAll(data)
        }
        notifyDataSetChanged()
    }

    inner class IssueViewHolder(private val binding: ItemIssueBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.typeView.setCornerRadiusOfView(5f.dpf())
        }
        fun bind(issue : Issue) {
            with(binding) {
                typeView.setBackgroundColor(
                    typeView.context.getColor(issue.type.color)
                )
                label.text = issue.label
                time.text = issue.workTime.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : IssueViewHolder {
        val binding = ItemIssueBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return IssueViewHolder(binding)
    }

    override fun onBindViewHolder(holder : IssueViewHolder, position : Int) = holder.bind(issues[position])
    override fun getItemCount() : Int = issues.size

}