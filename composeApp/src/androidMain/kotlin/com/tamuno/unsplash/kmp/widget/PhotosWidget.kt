package com.tamuno.unsplash.kmp.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalSize
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.GridCells
import androidx.glance.appwidget.lazy.LazyVerticalGrid
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import java.io.File

internal class PhotosWidget : GlanceAppWidget() {

    override val stateDefinition = PreferencesGlanceStateDefinition

    override val sizeMode =
        SizeMode.Responsive(
            setOf(
                DpSize(100.dp, 100.dp),
                DpSize(150.dp, 150.dp),
                DpSize(200.dp, 200.dp),
                DpSize(300.dp, 300.dp),
                DpSize(400.dp, 400.dp),
                DpSize(400.dp, 600.dp),
            )
        )

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent { WidgetContent() }
    }
}

@Composable
private fun WidgetContent() {

    val prefs = currentState<Preferences>()
    val entries = prefs[PHOTOS_KEY] ?: emptySet()
    val size = LocalSize.current

    val photos = mapEntriesToPhotos(entries)

    Column(
        modifier = GlanceModifier.fillMaxSize().background(Color.White).padding(8.dp),
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
    ) {
        Text(
            text = "Favourites",
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
            modifier = GlanceModifier.padding(bottom = 8.dp),
        )

        if (photos.isEmpty()) {
            Box(modifier = GlanceModifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No favourites yet")
            }
        } else {
            val columns =
                when {
                    size.width >= 400.dp -> 4
                    size.width >= 300.dp -> 3
                    size.width >= 200.dp -> 2
                    else -> 1
                }

            LazyVerticalGrid(
                gridCells = GridCells.Fixed(columns),
                modifier = GlanceModifier.fillMaxSize(),
            ) {
                items(photos) { photo -> PhotoItem(photo) }
            }
        }
    }
}

@Composable
private fun PhotoItem(photo: WidgetPhoto) {

    val file = File(photo.path)

    if (!file.exists()) return

    val bitmap = decodeSampledBitmap(file.absolutePath, 200)

    if (bitmap != null) {
        Box(modifier = GlanceModifier.padding(6.dp).cornerRadius(12.dp)) {
            Image(
                provider = ImageProvider(bitmap),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = GlanceModifier.fillMaxWidth().height(100.dp).cornerRadius(12.dp),
            )
        }
    }
}
