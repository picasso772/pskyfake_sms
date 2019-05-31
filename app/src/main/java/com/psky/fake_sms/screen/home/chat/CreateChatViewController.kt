package com.psky.fake_sms.screen.home.chat

import butterknife.OnClick
import com.psky.fake_sms.R
import com.psky.fake_sms.screen.base.BaseFragment
import com.psky.fake_sms.service.ScreenService
import com.psky.fake_sms.uicomponent.PopupSelectImageView
import com.psky.fake_sms.utils.DataUtils
import com.psky.fake_sms.utils.hideKeyboard
import kotlinx.android.synthetic.main.create_chat_view_controller.*

class CreateChatViewController : BaseFragment() {

    // region -> Actions

    @OnClick(R.id.layoutView) fun actionLayoutView(){
        textFieldName.hideKeyboard()
        textFieldPhone.hideKeyboard()
    }

    @OnClick(R.id.buttonImage) fun actionSelectImage(){
        DataUtils.shared.backID = 1
        textFieldName.hideKeyboard()
        textFieldPhone.hideKeyboard()

        textFieldName.isSelected = false
        ScreenService.shared.setSubView(PopupSelectImageView::class)
    }

    // endregion
}