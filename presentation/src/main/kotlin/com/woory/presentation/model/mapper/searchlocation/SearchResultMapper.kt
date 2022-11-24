package com.woory.presentation.model.mapper.searchlocation

import com.woory.data.model.LocationModel
import com.woory.data.model.LocationSearchModel
import com.woory.presentation.model.mapper.UiModelMapper
import com.woory.presentation.model.mapper.location.asDomain
import com.woory.presentation.model.mapper.location.asUiModel
import com.woory.presentation.ui.creatingpromise.LocationSearchResult

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