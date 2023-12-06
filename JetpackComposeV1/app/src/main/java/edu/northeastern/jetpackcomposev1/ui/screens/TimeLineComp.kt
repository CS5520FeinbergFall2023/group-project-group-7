package edu.northeastern.jetpackcomposev1.application

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import edu.northeastern.jetpackcomposev1.R

import androidx.compose.ui.graphics.drawscope.Stroke
import edu.northeastern.jetpackcomposev1.models.application.Event
import edu.northeastern.jetpackcomposev1.models.application.TimeLine
import edu.northeastern.jetpackcomposev1.models.application.timeLine.CircleParameters
import edu.northeastern.jetpackcomposev1.models.application.timeLine.LineParameters
import edu.northeastern.jetpackcomposev1.models.application.timeLine.StrokeParameters
import java.time.format.DateTimeFormatter


object CircleParametersDefaults {

    private val defaultCircleRadius = 12.dp

    fun circleParameters(
        radius: Dp = defaultCircleRadius,
        backgroundColor: Color = Color.Cyan,
        stroke: StrokeParameters? = null,
        @DrawableRes
        icon: Int? = null
    ) = CircleParameters(
        radius,
        backgroundColor,
        stroke,
        icon
    )
}

object LineParametersDefaults {

    private val defaultStrokeWidth = 3.dp

    fun linearGradient(
        strokeWidth: Dp = defaultStrokeWidth,
        startColor: Color,
        endColor: Color,
        startY: Float = 0.0f,
        endY: Float = Float.POSITIVE_INFINITY
    ): LineParameters {
        val brush = Brush.verticalGradient(
            colors = listOf(startColor, endColor),
            startY = startY,
            endY = endY
        )
        return LineParameters(strokeWidth, brush)
    }
}

@Composable
fun TimelineNode(
    circleParameters: CircleParameters,
    lineParameters: LineParameters? = null,
    contentStartOffset: Dp = 16.dp,
    spacer: Dp = 32.dp,
    content: @Composable BoxScope.(modifier: Modifier) -> Unit
) {
    val iconPainter = circleParameters.icon?.let { painterResource(id = it) }
    Box(
        modifier = Modifier
            .wrapContentSize()
            .drawBehind {
                val circleRadiusInPx = circleParameters.radius.toPx()

                lineParameters?.let {
                    drawLine(
                        brush = it.brush,
                        start = Offset(x = circleRadiusInPx, y = circleRadiusInPx * 2),
                        end = Offset(x = circleRadiusInPx, y = this.size.height),
                        strokeWidth = it.strokeWidth.toPx()
                    )
                }

                drawCircle(
                    circleParameters.backgroundColor,
                    circleRadiusInPx,
                    center = Offset(x = circleRadiusInPx, y = circleRadiusInPx)
                )

                circleParameters.stroke?.let { stroke ->
                    val strokeWidthInPx = stroke.width.toPx()
                    drawCircle(
                        color = stroke.color,
                        radius = circleRadiusInPx - strokeWidthInPx / 2,
                        center = Offset(x = circleRadiusInPx, y = circleRadiusInPx),
                        style = Stroke(width = strokeWidthInPx)
                    )
                }

                iconPainter?.let { painter ->
                    this.withTransform(
                        transformBlock = {
                            translate(
                                left = circleRadiusInPx - painter.intrinsicSize.width / 2f,
                                top = circleRadiusInPx - painter.intrinsicSize.height / 2f
                            )
                        },
                        drawBlock = {
                            this.drawIntoCanvas {
                                with(painter) {
                                    draw(intrinsicSize)
                                }
                            }

                        })
                }
            }

    ) {
        content(
            Modifier
                .defaultMinSize(minHeight = circleParameters.radius * 2)
                .padding(
                    start = circleParameters.radius * 2 + contentStartOffset,
                    bottom = spacer
                )
        )
    }
}

enum class NodeColors(val modifiedColor: Color) {
    MAGENTA(Color.Magenta.copy(alpha = 0.4f)),
    YELLOW(Color.Yellow.copy(alpha = 0.4f)),
    CYAN(Color.Cyan.copy(alpha = 0.4f)),
    RED(Color.Red.copy(alpha = 0.4f))
}


@Composable
fun TimelineComp(
    timeLine: TimeLine,
    onDeleteClicked: (Event) -> Unit = {},
    onEditClicked: (Event) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)

    ) {

        if (timeLine.count == 0) {
            Text(text = "No events")
        }
        if (timeLine.count == 1) {
            LastNode(timeLine, onEditClicked, onDeleteClicked)
        } else {

            timeLine.results.forEachIndexed { index, event ->
                val color = NodeColors.values()[index % NodeColors.values().size].modifiedColor
                val nextColor =
                    NodeColors.values()[(index + 1) % NodeColors.values().size].modifiedColor

                if (index != timeLine.count - 1) {
                    TimelineNode(
                        circleParameters = CircleParametersDefaults.circleParameters(
                            backgroundColor = color,
                        ),
                        lineParameters = LineParametersDefaults.linearGradient(
                            startColor = color,
                            endColor = nextColor
                        )
                    ) { modifier ->
                        EventCard(
                            modifier,
                            containerColor = color,
                            event = event,
                            onEditClicked = { onEditClicked(it) },
                            onDeleteClicked = { onDeleteClicked(it) }
                        )
                    }

                } else {
                    LastNode(timeLine, onEditClicked, onDeleteClicked)
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EventCard(
    modifier: Modifier, containerColor: Color, event: Event,
    onEditClicked: (Event) -> Unit,
    onDeleteClicked: (Event) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.CenterVertically)
            .clickable(onClick = { onEditClicked(event) }),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = event.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(3f),
                color = Color.Black,
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = event.status,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(3f),
                color = Color.Black,
                style = MaterialTheme.typography.bodyMedium,
            )
            IconButton(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .weight(1f),
                onClick = { onDeleteClicked(event) },
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}

@Composable
fun LastNode(
    timeLine: TimeLine,
    onEditClicked: (Event) -> Unit,
    onDeleteClicked: (Event) -> Unit
) {
    TimelineNode(
        circleParameters = CircleParametersDefaults.circleParameters(
            backgroundColor = Color.Green,
            stroke = StrokeParameters(color = Color.Blue, width = 2.dp),
            icon = R.drawable.ic_bubble_warning_16
        ),
        ) { modifier ->
        EventCard(
            modifier,
            containerColor = Color.Green.copy(alpha = 0.5f),
            event = timeLine.results[timeLine.results.size - 1],
            onEditClicked = { onEditClicked(it) },
            onDeleteClicked = { onDeleteClicked(it) }
        )
    }
}

