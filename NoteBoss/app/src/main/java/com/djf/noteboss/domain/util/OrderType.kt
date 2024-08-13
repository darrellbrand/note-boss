package com.djf.noteboss.domain.util

sealed class OrderType {
    data object Ascending: OrderType()
    data object Descending: OrderType()
}

sealed class NoteOrder (val orderType: OrderType){
    class Title(orderType: OrderType): NoteOrder(orderType)
    class Date(orderType: OrderType): NoteOrder(orderType)
    class Color(orderType: OrderType): NoteOrder(orderType)
}