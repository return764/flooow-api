package com.cn.tg.flooow.model.action

class ActionComposite(
    private val actions: MutableSet<Action> = mutableSetOf()
): Action{

    fun add(action: Action) {
        actions.add(action)
    }

    override fun run(actionChains: ActionChains, arg: String): String {
        TODO("Not yet implemented")
    }
}
