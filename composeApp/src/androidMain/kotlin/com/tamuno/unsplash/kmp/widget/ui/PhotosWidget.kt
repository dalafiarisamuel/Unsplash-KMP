package com.tamuno.unsplash.kmp.widget.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalSize
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.GridCells
import androidx.glance.appwidget.lazy.LazyVerticalGrid
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.tamuno.unsplash.kmp.MainActivity
import com.tamuno.unsplash.kmp.R
import com.tamuno.unsplash.kmp.widget.data.PHOTOS_KEY
import com.tamuno.unsplash.kmp.widget.data.TOTAL_FAVOURITES_KEY
import com.tamuno.unsplash.kmp.widget.data.WidgetPhoto
import com.tamuno.unsplash.kmp.widget.util.decodeSampledBitmap
import com.tamuno.unsplash.kmp.widget.util.mapEntriesToPhotos
import ui.theme.ColorCrimsonRed
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
                DpSize(600.dp, 600.dp),
            )
        )

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent { GlanceTheme { WidgetContent() } }
    }
}

@Composable
private fun WidgetContent() {

    val prefs = currentState<Preferences>()
    val entries = prefs[PHOTOS_KEY] ?: emptySet()
    val totalCount = prefs[TOTAL_FAVOURITES_KEY] ?: 0
    val size = LocalSize.current

    val photos = remember(entries) { mapEntriesToPhotos(entries) }

    Column(
        modifier =
            GlanceModifier.fillMaxSize()
                .background(GlanceTheme.colors.widgetBackground)
                .cornerRadius(16.dp)
                .padding(8.dp)
                .clickable(actionStartActivity<MainActivity>()),
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
    ) {
        Row(
            modifier = GlanceModifier.padding(top = 8.dp, bottom = 12.dp),
            verticalAlignment = Alignment.Vertical.CenterVertically,
        ) {
            Image(
                provider = ImageProvider(R.drawable.ic_favorite),
                contentDescription = null,
                colorFilter =
                    ColorFilter.tint(ColorProvider(day = ColorCrimsonRed, night = ColorCrimsonRed)),
                modifier = GlanceModifier.size(20.dp).padding(end = 6.dp),
            )
            Text(
                text = "Favourites (${photos.size} / $totalCount)",
                style = TextStyle(color = GlanceTheme.colors.onSurface, fontSize = 11.sp),
            )
        }

        if (photos.isEmpty()) {
            Box(modifier = GlanceModifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No favourites yet",
                    style = TextStyle(color = GlanceTheme.colors.onSurfaceVariant),
                )
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

    val bitmap =
        remember(photo.path) {
            val file = File(photo.path)
            if (file.exists()) {
                decodeSampledBitmap(file.absolutePath, 200)
            } else {
                null
            }
        }

    if (bitmap != null) {
        Box(modifier = GlanceModifier.padding(4.dp)) {
            Image(
                provider = ImageProvider(bitmap),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = GlanceModifier.fillMaxWidth().height(100.dp).cornerRadius(12.dp),
            )
        }
    }
}
