package com.psky.fake_sms.screen.show.detail

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.psky.fake_sms.R
import com.psky.fake_sms.adapter.show.MessengerChatAdapter
import com.psky.fake_sms.screen.base.BaseFragment
import com.psky.fake_sms.screen.chat.StatusSelect
import com.psky.fake_sms.service.ScreenService
import com.psky.fake_sms.utils.DataUtils
import com.psky.fake_sms.utils.Define
import com.psky.fake_sms.utils.hideKeyboard
import kotlinx.android.synthetic.main.messenger_chat_view_controller.*
import java.io.File

class MessengerChatViewController : BaseFragment() {
    private lateinit var adapter: MessengerChatAdapter
    private lateinit var viewContent: View
    private var isShowRate = false

    private var prefs : SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var countUse: Int = 0

    @SuppressLint("CommitPrefEdits")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DataUtils.shared.backID = 11
        viewContent = view
        viewContent.translationX = view.rootView?.width?.toFloat() ?: 0F

        animationOpenView()
        initView()
    }

    @OnClick(R.id.buttonBack) fun actionBack() {
        // reset data
        DataUtils.shared.resetData()
        // remove view
        DataUtils.shared.backID = 6
        // TODO
        val fragments = fragmentManager?.fragments ?: return
        var frs: MessengerChatViewController? = null
        for (fr in fragments) {
            if (fr is MessengerChatViewController) {
                frs = fr
                break
            }
        }

        frs?.animationHideView()
    }

    @OnClick(R.id.layoutRate) fun actionRate() {
        return
    }

    @OnClick(R.id.tvNo) fun actionNoRate() {
        animationHideLayoutRate()
        editor!!.putString(Define.Fields.STATUS_RATE , Define.Fields.STATUS_RATE_NO).apply()
        editor!!.putInt(Define.Fields.RATE_NO_NEXT, countUse.plus(15)).apply()
    }

    @OnClick(R.id.tvRateNow) fun actionRateNow() {
        animationHideLayoutRate()
        editor!!.putString(Define.Fields.STATUS_RATE , Define.Fields.STATUS_RATE_YES).apply()
        editor!!.putInt(Define.Fields.RATE_YES_NEXT, countUse.plus(20)).apply()
        val uri: Uri = Uri.parse("market://details?id=${context?.packageName}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=${context?.packageName}"))
            )
        }
    }

    @OnClick(R.id.tvLater) fun actionRateLater() {
        animationHideLayoutRate()
        editor!!.putString(Define.Fields.STATUS_RATE , Define.Fields.STATUS_RATE_LATER).apply()
        editor!!.putInt(Define.Fields.RATE_LATER_NEXT, countUse.plus(10)).apply()
    }

    private fun animationHideLayoutRate() {
        layoutRate.animate().setDuration(300L).alpha(0F).withEndAction {
            layoutRate.visibility = View.GONE
            layoutRate.alpha = 0F
        }
    }

    private fun initView() {
        val user = DataUtils.shared.user ?: return
        Glide.with(logoUser)
            .load(File(user.image))
            .into(logoUser)

        title.text = user.name

        recyclerMessages.layoutManager = LinearLayoutManager(context!!)

        recyclerMessages.setHasFixedSize(true)
        val messages = DataUtils.shared.messages ?: return
        adapter = MessengerChatAdapter(messages, context = context)

        recyclerMessages.adapter = adapter
        title.hideKeyboard()
    }

    private fun animationOpenView() {
        DataUtils.shared.backID = 11
        // tăng lượt sử dụng lên 1
        prefs = context?.getSharedPreferences(Define.Fields.PREFS_FILENAME,0)
        if (prefs != null) {
            editor = prefs!!.edit()

            val countUseMessenger = prefs!!.getInt("countUseMessenger", 0)
            countUse = countUseMessenger.plus(1)
            editor!!.putInt("countUseMessenger", countUse)
            editor!!.apply()

            when (prefs!!.getString(Define.Fields.STATUS_RATE, Define.Fields.STATUS_RATE_DEFAULT)) {
                Define.Fields.STATUS_RATE_DEFAULT -> {
                    isShowRate = true
                }
                Define.Fields.STATUS_RATE_LATER -> {
                    val indexNext = prefs!!.getInt(Define.Fields.RATE_LATER_NEXT, 0)
                    if (indexNext <= countUse) {
                        isShowRate = true
                    }
                }
                Define.Fields.STATUS_RATE_NO -> {
                    val indexNext = prefs!!.getInt(Define.Fields.RATE_NO_NEXT, 0)
                    if (indexNext <= countUse) {
                        isShowRate = true
                    }
                }
                Define.Fields.STATUS_RATE_YES -> {
                    val indexNext = prefs!!.getInt(Define.Fields.RATE_YES_NEXT, 0)
                    if (indexNext <= countUse) {
                        isShowRate = true
                    }
                }
            }
        }
        viewContent.animate().setDuration(300L).translationX(0f).withEndAction {
            checkConditionRate()
        }
    }


    /**
     * Used to check the condition of displaying the view rate
     */
    private fun checkConditionRate() {
        if (isShowRate) {
            Handler().postDelayed({
                animationOpenLayoutRate()
            }, 200L)
        }
    }

    private fun animationOpenLayoutRate() {
        layoutRate.visibility = View.VISIBLE
        layoutRate.alpha = 0F
        layoutRate.animate().setDuration(300L).alpha(1F).start()
    }

    fun animationHideView() {
        viewContent.animate().setDuration(300L).translationX(viewContent.width.toFloat()).withEndAction {
            ScreenService.shared.removeSubView()
        }.start()
    }
}