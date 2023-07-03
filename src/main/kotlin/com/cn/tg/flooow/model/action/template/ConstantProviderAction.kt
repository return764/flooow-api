package com.cn.tg.flooow.model.action.template

import com.cn.tg.flooow.model.action.AbstractAction
import com.cn.tg.flooow.model.action.ActionChains
import com.cn.tg.flooow.model.action.annotation.ActionMarker
import com.cn.tg.flooow.model.action.ActionOperator
import com.cn.tg.flooow.model.action.ProviderAction
import com.cn.tg.flooow.model.action.annotation.ActionOption

@ActionMarker(name = "constant", type = "provider", shape = "input", label="Constant Input")
class ConstantProviderAction: AbstractAction(), ProviderAction{

    @ActionOption(name = "value", defaultValue = "")
    private lateinit var value: String

    private lateinit var ao: ActionOperator
    override fun run(actionChains: ActionChains, arg: String): String {
        return arg
    }

}
