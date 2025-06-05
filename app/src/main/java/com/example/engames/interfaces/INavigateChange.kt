package com.example.engames.interfaces

import com.example.domain.utils.ActionHolder.ActionHolder.setActionId

interface INavigateChange {
    fun putNavigateId(id: Int){
        setActionId(id)
    }
}