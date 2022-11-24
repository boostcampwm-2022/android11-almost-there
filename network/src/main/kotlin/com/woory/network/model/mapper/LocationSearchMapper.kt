package com.woory.network.model.mapper

import com.woory.data.model.GeoPointModel
import com.woory.data.model.LocationModel
import com.woory.data.model.LocationSearchModel
import com.woory.network.model.Poi

object LocationSearchMapper : ModelMapper<LocationSearchModel, Poi> {
    override fun asDomain(model: Poi): LocationSearchModel =
        LocationSearchModel(
            name = model.name,
            address = LocationModel(
                geoPoint = GeoPointModel(model.noorLat.toDouble(), model.noorLon.toDouble()),
                address = model.newAddressList.newAddress[0].fullAddressRoad
            )
        )
}

fun Poi.asDomain() = LocationSearchMapper.asDomain(this)