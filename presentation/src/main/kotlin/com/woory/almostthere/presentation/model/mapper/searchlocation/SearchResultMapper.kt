package com.woory.almostthere.presentation.model.mapper.searchlocation

import com.woory.almostthere.data.model.LocationModel
import com.woory.almostthere.data.model.LocationSearchModel
import com.woory.almostthere.presentation.model.mapper.UiModelMapper
import com.woory.almostthere.presentation.model.mapper.location.asDomain
import com.woory.almostthere.presentation.model.mapper.location.asUiModel
import com.woory.almostthere.presentation.ui.creatingpromise.locationsearch.LocationSearchResult

object SearchResultMapper : UiModelMapper<LocationSearchResult, LocationSearchModel> {
    override fun asUiModel(domain: LocationSearchModel): LocationSearchResult =
        LocationSearchResult(
            name = domain.name,
            address = domain.address.address,
            location = domain.address.geoPoint.asUiModel()
        )

    override fun asDomain(uiModel: LocationSearchResult): LocationSearchModel =
        LocationSearchModel(
            name = uiModel.name,
            address = LocationModel(
                geoPoint = uiModel.location.asDomain(),
                address = uiModel.address
            )
        )
}