package com.example.domain.utils

object ActionHolder {
    object ActionHolder {
        private var actionId: Int = -1

        fun setActionId(id: Int) {
            actionId = id
        }
        fun getActionId() = actionId
    }
}