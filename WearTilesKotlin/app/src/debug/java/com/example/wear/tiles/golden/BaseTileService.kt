package com.example.wear.tiles.golden

import android.content.Context
import androidx.wear.protolayout.DeviceParametersBuilders.DeviceParameters
import androidx.wear.protolayout.LayoutElementBuilders.LayoutElement
import androidx.wear.protolayout.ResourceBuilders.Resources
import androidx.wear.protolayout.TimelineBuilders.Timeline
import androidx.wear.tiles.RequestBuilders.ResourcesRequest
import androidx.wear.tiles.RequestBuilders.TileRequest
import androidx.wear.tiles.TileBuilders.Tile
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.tiles.SuspendingTileService

// Denne konstant fungerer som en cache-nøgle for tile'ens ressourcer.
const val RESOURCES_VERSION = "1"

// Ved at nedarve fra Horologist's SuspendingTileService bliver det endnu nemmere.
// Alternativt kan du nedarve fra den almindelige androidx.wear.tiles.TileService
// og overskrive de to suspend-funktioner.
@OptIn(ExperimentalHorologistApi::class)
abstract class BaseTileService : SuspendingTileService() {

    // Den nye coroutine-baserede metode til at hente tile-layoutet.
    // Den kører på en baggrundstråd.
    override suspend fun tileRequest(requestParams: TileRequest): Tile {
        return Tile.Builder()
            .setResourcesVersion(RESOURCES_VERSION)
            .setTileTimeline(
                Timeline.fromLayoutElement(layout(this, requestParams.deviceConfiguration))
            )
            .build()
    }

    // Den nye coroutine-baserede metode til at hente ressourcer.
    override suspend fun resourcesRequest(requestParams: ResourcesRequest): Resources {
        return resources(this)(requestParams)
    }

    abstract fun layout(
        context: Context,
        deviceParameters: DeviceParameters
    ): LayoutElement

    abstract fun resources(context: Context): (ResourcesRequest) -> Resources
}
