package lib.kalu.adapter.model

import java.io.Serializable

/**
 * description: 分组
 * created by kalu on 2017/5/26 15:10
 */
abstract class TabModel<T> : Serializable {

    var isTab = false
    var tabName = ""
    var t: T? = null

    constructor(isTab: Boolean, tabName: String) {
        this.isTab = isTab
        this.tabName = tabName
        this.t = null
    }

    constructor(t: T) {
        this.t = t
    }
}
