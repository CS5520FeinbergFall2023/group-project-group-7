package edu.northeastern.jetpackcomposev1.ui.sheets

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.northeastern.jetpackcomposev1.viewmodels.JobViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterJobSheet(
    jobViewModel: JobViewModel,
    onCloseSheet: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = { onCloseSheet() },
        sheetState = sheetState,
    ) {
        // Sheet content
        LazyColumn(modifier = modifier
            .padding(horizontal = 8.dp).fillMaxHeight(0.95f)
        ) {
            item {
                /*TODO more on the way*/
                Text("This will show filter options")
                Text("More on the way")
                Spacer(modifier = modifier.height(64.dp))
            }
        }
    }
}