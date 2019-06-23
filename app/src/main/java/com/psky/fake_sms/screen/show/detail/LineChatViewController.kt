package com.psky.fake_sms.screen.show.detail

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.psky.fake_sms.R
import com.psky.fake_sms.adapter.show.LineChatAdapter
import com.psky.fake_sms.adapter.show.MessagesShowChatAdapter
import com.psky.fake_sms.screen.base.BaseFragment
import com.psky.fake_sms.service.ScreenService
import com.psky.fake_sms.utils.DataUtils
import com.psky.fake_sms.utils.hideKeyboard
import kotlinx.android.synthetic.main.line_chat_view_controller.*

class LineChatViewController : BaseFragment() {
    private lateinit var adapter: LineChatAdapter
    private lateinit var viewContent: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DataUtils.shared.backID = 12
        viewContent = view
        viewContent.translationX = view.rootView?.width?.toFloat() ?: 0F

        animationOpenView()
        initView()
    }

    @OnClick(R.id.buttonBack) fun actionBack() {
        // reset data
        DataUtils.shared.resetData()
        // remove view
        DataUtils.shared.backID = 7
        // TODO
        val fragments = fragmentManager?.fragments ?: return
        var frs: LineChatViewController? = null
        for (fr in fragments) {
            if (fr is LineChatViewController) {
                frs = fr
                break
            }
        }

        frs?.animationHideView()
    }

    private fun initView() {
        val user = DataUtils.shared.user ?: return

        title.text = user.name

        recyclerMessages.layoutManager = LinearLayoutManager(context!!)

        recyclerMessages.setHasFixedSize(true)
        val messages = DataUtils.shared.messages ?: return
        adapter = LineChatAdapter(messages, context = context)

        recyclerMessages.adapter = adapter
        recyclerMessages.hideKeyboard()
    }

    private fun animationOpenView() {
        DataUtils.shared.backID = 12
        viewContent.animate().setDuration(300L).translationX(0f).start()
    }

    fun animationHideView() {
        viewContent.animate().setDuration(300L).translationX(viewContent.width.toFloat()).withEndAction {
            ScreenService.shared.removeSubView()
        }.start()
    }
}