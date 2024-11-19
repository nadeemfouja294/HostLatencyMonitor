package com.logical.hostlatencymonitor.data.mapper

import com.logical.hostlatencymonitor.data.model.HostDataModel
import com.logical.hostlatencymonitor.domain.model.Host

fun HostDataModel.toDomainModel(): Host {
    return Host(
        name = this.name,
        url = this.url,
        icon = this.icon
    )
}
