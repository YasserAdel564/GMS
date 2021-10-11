package com.gms.app.data.storage.remote.model.programs


class ProgrammePeriodModel(
    var id: Int,
    var period: String,
) {
    override fun toString(): String {
        return period
    }
}
