package edu.northeastern.jetpackcomposev1.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.northeastern.jetpackcomposev1.R


@Composable
fun AboutScreen() {
    val names = listOf("Bowen Xu", "Jun Wang", "Qiaochu Zhang", "Shiqi Feng", "Yiwei Wang")
    val context = LocalContext.current
    val url = context.getString(R.string.github)
    val intent = remember { Intent(Intent.ACTION_VIEW, Uri.parse(url)) }

    Surface(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item{
                Image(
                    modifier = Modifier.size(200.dp),
                    painter = painterResource(id = R.drawable.biglogo),
                    contentDescription = "Logo"
                )
                Text(
                    text = "Job Track Pro",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Apply, Track, Thrive: Your Job Search Companion",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))


                Text(
                    text = "Group 7 ",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top= 32.dp, bottom = 8.dp)
                )

                Text(
                    text = "Our Team Members: ",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                names.forEach {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                Button(onClick = { context.startActivity(intent) }) {
                    Text(text = "Navigate to Our Github Repo")
                }
            }
        }
    }
}