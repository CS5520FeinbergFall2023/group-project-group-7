package edu.northeastern.jetpackcomposev1.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rizzi.bouquet.HorizontalPDFReader
import com.rizzi.bouquet.HorizontalPdfReaderState
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.VerticalPDFReader
import com.rizzi.bouquet.VerticalPdfReaderState
import edu.northeastern.jetpackcomposev1.viewmodels.ResumeViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PDFViewScreen(viewModel : ResumeViewModel, onNavigateToResumeManagement: () -> Unit,) {
    val state = viewModel.bouquetViewModel.stateFlow.collectAsState()
    Column {
        IconButton(onClick = onNavigateToResumeManagement) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "back to resume management"
            )
        }
        Box(modifier = Modifier.padding(10.dp,0.dp).fillMaxSize()) {
            when (val actualState = state.value) {
                is VerticalPdfReaderState -> PDFView(
                    pdfState = actualState,
                )
                is HorizontalPdfReaderState -> HPDFView(
                    pdfState = actualState,
                )
            }
        }
    }
}

@Composable
fun PDFView(
    pdfState: VerticalPdfReaderState,
) {
    Box(
        contentAlignment = Alignment.TopStart
    ) {
        VerticalPDFReader(
            state = pdfState,
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
        )
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            LinearProgressIndicator(
                progress = pdfState.loadPercent / 100f,
                color = Color.DarkGray,
                trackColor = Color.Green,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun HPDFView(
    pdfState: HorizontalPdfReaderState
) {
    Box(
        contentAlignment = Alignment.TopStart
    ) {
        HorizontalPDFReader(
            state = pdfState,
            modifier = Modifier
                .fillMaxSize()
        )
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            LinearProgressIndicator(
                progress = pdfState.loadPercent / 100f,
                color = Color.DarkGray,
                trackColor = Color.Green,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}