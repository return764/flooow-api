package com.cn.tg.flooow.model.action.template

import com.cn.tg.flooow.model.action.AbstractAction
import com.cn.tg.flooow.model.action.Action
import com.cn.tg.flooow.model.action.annotation.ActionMarker
import com.cn.tg.flooow.model.action.annotation.ActionOption
import com.cn.tg.flooow.model.action.annotation.ActionReturn
import com.cn.tg.flooow.model.action.annotation.ActionReturns

@ActionMarker(name = "constant", type = "provider", shape = "input", label="Constant Input")
@ActionReturns([
    ActionReturn(name = "value", type = String::class)
])
class ConstantProviderAction: AbstractAction(), Action {

    @ActionOption(name = "value", defaultValue = "test")
    private lateinit var value: String

    override fun run() {
        Thread.sleep(1500);
        ctx.returnValue(this, value)
    }

}
