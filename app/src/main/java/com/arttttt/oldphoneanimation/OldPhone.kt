package com.arttttt.oldphoneanimation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import com.arttttt.oldphoneanimation.ui.theme.OldPhoneAnimationTheme
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

private class DigitBounds(
    var left: Float = 0f,
    var top: Float = 0f,
    var right: Float = 0f,
    var bottom: Float = 0f
) {
    fun contains(point: Offset): Boolean {
        return point.x >= left && point.x <= right &&
                point.y >= top && point.y <= bottom
    }

    fun update(x: Float, y: Float, size: Float) {
        left = x - size / 2
        top = y - size / 2
        right = x + size / 2
        bottom = y + size / 2
    }
}

private val digits = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0")

@Composable
fun OldPhone(
    modifier: Modifier,
    onDigitSelected: (String) -> Unit,
) {
    var rotationAngle by remember { mutableFloatStateOf(0f) }
    val textMeasurer = rememberTextMeasurer()
    val typography = MaterialTheme.typography.headlineLarge
    var activeDigitIndex by remember { mutableIntStateOf(-1) }

    val digitBoundsList = remember { List(digits.size) { DigitBounds() } }

    val animatedRotationAngle by animateFloatAsState(
        targetValue = rotationAngle
    )

    val texts = remember {
        digits.map { digit ->
            textMeasurer.measure(
                text = digit,
                style = typography,
            )
        }
    }

    val baseColor = Color(0xFFF5F2EA)
    val coverColor = Color(0xFF2B2522).copy(alpha = 0.92f)
    val centerAndFrameColor = Color(0xFFE5E0D5)

    Canvas(
        modifier = modifier
            .padding(4.dp)
            .aspectRatio(1f)
            .pointerInput(Unit) {
                awaitEachGesture {
                    val down = awaitFirstDown()

                    val rotatedPoint = down.position.rotate(
                        center = size.center,
                        degrees = -rotationAngle,
                    )

                    val digitHit = digitBoundsList.indexOfFirst { it.contains(rotatedPoint) }
                    activeDigitIndex = digitHit

                    if (digitHit != -1) {
                        var currentAngle = calculateAngle(down.position, size.center)
                        val maxRotation = (digitHit + 1) * 27f

                        do {
                            val event = awaitPointerEvent()
                            val newAngle = calculateAngle(event.changes.first().position, size.center)
                            val deltaAngle = (newAngle - currentAngle).let { delta ->
                                when {
                                    delta > 180f -> delta - 360f
                                    delta < -180f -> delta + 360f
                                    else -> delta
                                }
                            }

                            val targetRotation = rotationAngle + deltaAngle

                            if (rotationAngle <= maxRotation) {
                                rotationAngle = targetRotation.coerceIn(0f, maxRotation)
                            }

                            currentAngle = newAngle
                            event.changes.first().consume()
                        } while (event.changes.any { it.pressed })

                        val isDigitFullySelected = rotationAngle >= maxRotation * 0.9f
                        if (isDigitFullySelected) {
                            onDigitSelected(digits[digitHit])
                        }

                        rotationAngle = 0f
                        activeDigitIndex = -1
                    }
                }
            }
    ) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.width / 2f - 40.dp.toPx()
        val digitSize = 64.dp.toPx()
        val angleStep = -270f / digits.size

        drawCircle(
            color = baseColor,
        )

        digits.forEachIndexed { index, digit ->
            val angleInDegrees = index * angleStep - 45
            val angleInRadians = Math.toRadians(angleInDegrees.toDouble())

            digitBoundsList[index].update(
                center.x + radius * cos(angleInRadians).toFloat(),
                center.y + radius * sin(angleInRadians).toFloat(),
                digitSize,
            )

            val bounds = digitBoundsList[index]
            val x = (bounds.left + bounds.right) / 2
            val y = (bounds.top + bounds.bottom) / 2

            val textLayoutResult: TextLayoutResult = texts[index]

            drawText(
                textLayoutResult = textLayoutResult,
                topLeft = Offset(
                    x - textLayoutResult.size.width / 2f,
                    y - textLayoutResult.size.height / 2f,
                ),
            )
        }

        rotate(animatedRotationAngle) {
            val centerCircleRadius = size.width / 3.21f

            drawCircle(
                color = centerAndFrameColor,
                radius = centerCircleRadius,
                center = center
            )

            val dotsCount = 12
            val dotRadius = 2.dp.toPx()
            val dotDistance = centerCircleRadius - 10.dp.toPx()

            repeat(dotsCount) { index ->
                val angle = (360f / dotsCount) * index
                val angleRad = Math.toRadians(angle.toDouble())
                val dotX = center.x + dotDistance * cos(angleRad).toFloat()
                val dotY = center.y + dotDistance * sin(angleRad).toFloat()

                drawCircle(
                    color = Color.Gray,
                    radius = dotRadius,
                    center = Offset(dotX, dotY)
                )
            }

            val diskPath = Path().apply {
                addOval(
                    Rect(
                        center = center,
                        radius = size.width / 2f
                    )
                )

                digits.forEachIndexed { index, _ ->
                    val angleInDegrees = index * angleStep - 45
                    val angleInRadians = Math.toRadians(angleInDegrees.toDouble())

                    val x = center.x + radius * cos(angleInRadians).toFloat()
                    val y = center.y + radius * sin(angleInRadians).toFloat()

                    addOval(
                        Rect(
                            center = Offset(x, y),
                            radius = digitSize / 2f
                        )
                    )
                }

                addOval(
                    Rect(
                        center = center,
                        radius = centerCircleRadius
                    )
                )

                fillType = PathFillType.EvenOdd
            }

            drawPath(
                path = diskPath,
                color = coverColor,
            )

        }

        drawCircle(
            color = centerAndFrameColor,
            radius = size.width / 2f,
            center = center,
            style = Stroke(
                width = 8.dp.toPx(),
            ),
        )

        val stopperAngle = 0.0
        val stopperOuterDistance = size.width / 2f
        val stopperInnerDistance = size.width / 2f - 48.dp.toPx()

        val outerLeftX = center.x + stopperOuterDistance * cos(Math.toRadians(stopperAngle - 5).toDouble()).toFloat()
        val outerLeftY = center.y + stopperOuterDistance * sin(Math.toRadians(stopperAngle - 5).toDouble()).toFloat()
        val outerRightX = center.x + stopperOuterDistance * cos(Math.toRadians(stopperAngle + 5).toDouble()).toFloat()
        val outerRightY = center.y + stopperOuterDistance * sin(Math.toRadians(stopperAngle + 5).toDouble()).toFloat()

        val innerLeftX = center.x + stopperInnerDistance * cos(Math.toRadians(stopperAngle - 2.5).toDouble()).toFloat()
        val innerLeftY = center.y + stopperInnerDistance * sin(Math.toRadians(stopperAngle - 2.5).toDouble()).toFloat()
        val innerRightX = center.x + stopperInnerDistance * cos(Math.toRadians(stopperAngle + 2.5).toDouble()).toFloat()
        val innerRightY = center.y + stopperInnerDistance * sin(Math.toRadians(stopperAngle + 2.5).toDouble()).toFloat()

        drawPath(
            path = Path().apply {
                moveTo(outerLeftX, outerLeftY)
                lineTo(outerRightX, outerRightY)
                lineTo(innerRightX, innerRightY)
                lineTo(innerLeftX, innerLeftY)
                close()
            },
            color = centerAndFrameColor,
        )
    }
}

private fun calculateAngle(position: Offset, center: IntOffset): Float {
    val dx = position.x - center.x
    val dy = position.y - center.y
    return Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat()
}

fun Offset.rotate(
    center: IntOffset,
    degrees: Float,
): Offset {
    val angle = Math.toRadians(degrees.toDouble())
    val cos = cos(angle).toFloat()
    val sin = sin(angle).toFloat()

    val x = this.x - center.x
    val y = this.y - center.y

    return Offset(
        x = center.x + (x * cos - y * sin),
        y = center.y + (x * sin + y * cos)
    )
}

@Preview
@Composable
private fun OldPhonePreview() {
    OldPhoneAnimationTheme {
        OldPhone(
            modifier = Modifier,
            onDigitSelected = {},
        )
    }
}