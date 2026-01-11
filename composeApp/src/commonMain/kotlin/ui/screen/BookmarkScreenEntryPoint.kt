package ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ClearAll
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.ui.model.Photo
import org.koin.compose.viewmodel.koinViewModel
import ui.theme.appWhite
import ui.viewmodel.BookmarkScreenViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
internal fun BookmarkScreenEntryPoint(
    onBackPressed: () -> Unit = {},
    onItemClicked: (Photo) -> Unit,
    viewModel: BookmarkScreenViewModel = koinViewModel<BookmarkScreenViewModel>(),
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.size(50.dp),
                onClick = viewModel::clearAllPhoto,
            ) {
                Icon(
                    imageVector = Icons.Rounded.ClearAll,
                    tint = appWhite,
                    contentDescription = null,
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) {
        Bookmark(
            modifier = Modifier.fillMaxSize(),
            onBackPressed = onBackPressed,
            imageList = viewModel.photos,
            onItemClicked = onItemClicked,
        )
    }
}
