package ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import data.ui.model.ChipData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.stringResource
import ui.theme.UnsplashKMPTheme
import ui.theme.appDark
import ui.theme.appWhite
import unsplashkmp.composeapp.generated.resources.Res
import unsplashkmp.composeapp.generated.resources.nature
import unsplashkmp.composeapp.generated.resources.nature_emoji
import unsplashkmp.composeapp.generated.resources.photography
import unsplashkmp.composeapp.generated.resources.photography_emoji

@Composable
internal fun Chip(
    chip: ChipData =
        ChipData(
            stringResource(Res.string.photography_emoji),
            stringResource(Res.string.photography),
        ),
    isSelected: Boolean = true,
    onSelectionChanged: (String) -> Unit = {},
) {

    Surface(
        modifier = Modifier.padding(4.dp),
        elevation = 1.5.dp,
        shape = RoundedCornerShape(60),
        color = if (isSelected) appWhite else appDark,
    ) {
        Row(
            modifier =
                Modifier.toggleable(
                    value = isSelected,
                    onValueChange = {
                        if (!it) {
                            onSelectionChanged("")
                            return@toggleable
                        }
                        onSelectionChanged(chip.chipText)
                    },
                )
        ) {
            Text(
                text = chip.displayText,
                style = MaterialTheme.typography.caption,
                color = if (isSelected) appDark else appWhite,
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}

@Composable
internal fun ChipGroup(
    modifier: Modifier = Modifier,
    itemList: ImmutableList<ChipData> =
        persistentListOf(
            ChipData(
                stringResource(Res.string.photography_emoji),
                stringResource(Res.string.photography),
            ),
            ChipData(stringResource(Res.string.nature_emoji), stringResource(Res.string.nature)),
        ),
    onAddMoreChipClicked: () -> Unit = {},
    selectedText: String? = null,
    onSelectedChanged: (String) -> Unit = {},
) {
    Column(modifier = modifier.background(color = Color.Transparent)) {
        LazyRow(verticalAlignment = Alignment.CenterVertically) {
            items(itemList) { chip ->
                Chip(
                    chip = chip,
                    isSelected = chip.isChipSelected(selectedText),
                    onSelectionChanged = onSelectedChanged,
                )
            }
            item {
                Spacer(modifier = Modifier.padding(start = 10.dp))
                ChipButton(onClick = onAddMoreChipClicked)
                Spacer(modifier = Modifier.padding(end = 20.dp))
            }
        }
    }
}

@Composable
fun ChipComponent(
    modifier: Modifier = Modifier,
    onAddMoreChipClicked: () -> Unit = {},
    savedSearchQuery: List<ChipData> = emptyList(),
    selectedText: String? = null,
    textValueChange: ((String) -> Unit)? = null,
) {
    val staticList = GetStaticChipData()

    val list =
        remember(staticList, savedSearchQuery) { (staticList + savedSearchQuery).toImmutableList() }
    ChipGroup(
        modifier = modifier,
        selectedText = selectedText,
        onAddMoreChipClicked = onAddMoreChipClicked,
        itemList = list,
    ) {
        textValueChange?.invoke(it)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun PreviewChipComponent() {
    UnsplashKMPTheme { ChipComponent() }
}
