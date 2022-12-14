package com.woory.almostthere.network.model.mapper

import com.woory.almostthere.data.model.GeoPointModel
import com.woory.almostthere.data.model.LocationModel
import com.woory.almostthere.data.model.LocationSearchModel
import com.woory.almostthere.network.model.Poi

object LocationSearchMapper {

    fun asDomain(model: Poi): LocationSearchModel =
        LocationSearchModel(
            name = model.name,
            address = LocationModel(
                geoPoint = GeoPointModel(model.noorLat.toDouble(), model.noorLon.toDouble()),
                address = model.newAddressList.newAddress[0].fullAddressRoad
            )
        )
}

fun Poi.asDomain() = LocationSearchMapper.asDomain(this)