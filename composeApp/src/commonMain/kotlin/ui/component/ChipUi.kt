package ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import data.model.ui.ChipData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.theme.UnsplashKMPTheme
import ui.theme.appDark
import ui.theme.appWhite
import unsplashkmp.composeapp.generated.resources.*
import unsplashkmp.composeapp.generated.resources.Res


@Composable
internal fun Chip(
    chip: ChipData = ChipData(
        stringResource(Res.string.photography_emoji),
        stringResource(Res.string.photography)
    ),
    isSelected: Boolean = true,
    onSelectionChanged: (String) -> Unit = {},
) {

    Surface(
        modifier = Modifier.padding(4.dp),
        elevation = 3.dp,
        shape = RoundedCornerShape(50),
        color = if (isSelected) appWhite else appDark
    ) {
        Row(
            modifier = Modifier
            .toggleable(
                value = isSelected,
                onValueChange = {
                    if (!it) {
                        onSelectionChanged("")
                        return@toggleable
                    }
                    onSelectionChanged(chip.chipText)
                }
            )
        ) {
            Text(
                text = chip.displayText,
                style = MaterialTheme.typography.caption,
                color = if (isSelected) appDark else appWhite,
                modifier = Modifier.padding(8.dp)
            )
        }
    }

}


@Composable
internal fun ChipGroup(
    modifier: Modifier = Modifier,
    itemList: ImmutableList<ChipData> = persistentListOf(
        ChipData(
            stringResource(Res.string.photography_emoji),
            stringResource(Res.string.photography)
        ),
        ChipData(
            stringResource(Res.string.nature_emoji),
            stringResource(Res.string.nature)
        )
    ),
    selectedText: String? = null,
    onSelectedChanged: (String) -> Unit = {},
) {
    Column(
        modifier = modifier
            .background(color = Color.Transparent)
    ) {
        LazyRow {
            items(itemList) { chip ->
                Chip(
                    chip = chip,
                    isSelected = chip.isChipSelected(selectedText),
                    onSelectionChanged = onSelectedChanged
                )
            }
        }
    }
}

@Composable
fun ChipComponent(
    modifier: Modifier = Modifier,
    selectedText: String? = null,
    textValueChange: ((String) -> Unit)? = null,
) {

    ChipGroup(
        modifier = modifier,
        selectedText = selectedText,
        itemList = persistentListOf(
            ChipData(
                stringResource(Res.string.photography_emoji),
                stringResource(Res.string.photography)
            ),
            ChipData(
                stringResource(Res.string.nigeria_emoji),
                stringResource(Res.string.nigeria)
            ),
            ChipData(
                stringResource(Res.string.nature_emoji),
                stringResource(Res.string.nature)
            ),
            ChipData(
                stringResource(Res.string.fashion_emoji),
                stringResource(Res.string.fashion)
            ),
            ChipData(
                stringResource(Res.string.people_emoji),
                stringResource(Res.string.people)
            ),
            ChipData(
                stringResource(Res.string.technology_emoji),
                stringResource(Res.string.technology)
            ),
            ChipData(
                stringResource(Res.string.film_emoji),
                stringResource(Res.string.film)
            ),
            ChipData(
                stringResource(Res.string.travel_emoji),
                stringResource(Res.string.travel)
            ),
            ChipData(
                stringResource(Res.string.history_emoji),
                stringResource(Res.string.history)
            ),
            ChipData(
                stringResource(Res.string.animals_emoji),
                stringResource(Res.string.animals)
            ),
            ChipData(
                stringResource(Res.string.food_and_drink_emoji),
                stringResource(Res.string.food_and_drink)
            ),
            ChipData(
                stringResource(Res.string.spirituality_emoji),
                stringResource(Res.string.spirituality)
            ),
            ChipData(
                stringResource(Res.string.architecture_emoji),
                stringResource(Res.string.architecture)
            ),
            ChipData(
                stringResource(Res.string.business_and_work_emoji),
                stringResource(Res.string.business_and_work)
            ),
            ChipData(
                stringResource(Res.string.interior_emoji),
                stringResource(Res.string.interior)
            ),
            ChipData(
                stringResource(Res.string.experimental_emoji),
                stringResource(Res.string.experimental)
            ),
            ChipData(
                stringResource(Res.string.textures_and_patterns_emoji),
                stringResource(Res.string.textures_and_patterns)
            ),
        )
    ) {
        textValueChange?.invoke(it)
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun PreviewChipComponent() {
    UnsplashKMPTheme {
        ChipComponent()
    }
}
