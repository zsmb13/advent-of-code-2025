import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

var job: Job? = null

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(width = 1000.dp, height = 1000.dp),
        alwaysOnTop = true,
    ) {
        var target by remember { mutableIntStateOf(50) }
        val scope = rememberCoroutineScope()
        var count by remember { mutableIntStateOf(0) }

        var animDelay by remember { mutableIntStateOf(2000) }
        var fastMode by remember { mutableStateOf(false) }

        fun reset() {
            job?.cancel(); target = 50; count = 0; animDelay = 2000; fastMode = false
        }

        MaterialTheme(
            colors = darkColors()
        ) {
            Surface(
                Modifier.fillMaxSize(),
                color = Color(0x50000000)
            ) {
                Column(Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        OutlinedButton(onClick = { reset() }) {
                            Text("Reset")
                        }
//                        OutlinedButton(onClick = {
//                            reset()
//
//                            val testInput = listOf("L50", "L1", "R1", "R2", "L2", "L3", "R3", "R4", "L4", "L5", "R5")
//                            job = scope.launch {
//                                testInput.forEach {
//                                    target = if (it[0] == 'L') {
//                                        (target - it.drop(1).toInt())
//                                    } else {
//                                        (target + it.drop(1).toInt())
//                                    }
//
//                                    delay(3000)
//                                }
//                            }
//                        }) {
//                            Text("Run test")
//                        }
                        OutlinedButton(onClick = {
                            reset()

                            val testInput = readInput("Day01_test")
                            job = scope.launch {
                                animDelay = 2000
                                testInput.forEach {
                                    target = if (it[0] == 'L') {
                                        (target - it.drop(1).toInt())
                                    } else {
                                        (target + it.drop(1).toInt())
                                    }

//                                    if ((target % 100) == 0) {
//                                        count++
//                                    }

                                    delay(3000)
                                }
                            }
                        }) {
                            Text("Run part 1")
                        }
                        OutlinedButton(onClick = {
                            reset()

                            val testInput = readInput("Day01_test")
                            job = scope.launch {
                                animDelay = 80
                                fastMode = true
                                testInput.forEach {
                                    val delta = if (it[0] == 'L') -1 else 1
                                    repeat(it.drop(1).toInt()) {
                                        target = (target + delta)
                                        delay(100)

//                                        if ((target % 100) == 0) {
//                                            count++
//                                        }
                                    }
                                }
                            }
                        }) {
                            Text("Run part 2")
                        }
                    }

                    Text(
                        "Count: $count",
                        modifier = Modifier.padding(16.dp),
                        color = Color.Black,
                        fontSize = 48.sp,
                    )

                    Dial(
                        target = target,
                        fastMode = fastMode,
                        onCount = { count++ },
                        animDelay = animDelay,
                    )
                }
            }
        }
    }
}

@Composable
fun Dial(
    target: Int,
    fastMode: Boolean,
    onCount: () -> Unit,
    animDelay: Int,
) {
    val rotation = remember { Animatable(0f) }
    var highlight by remember { mutableStateOf(false) }
    LaunchedEffect(target) {
        if (!fastMode) {
            highlight = false
        }
        withContext(NonCancellable) {
            rotation.animateTo(-target / 100f * 360f, tween(animDelay))
        }
        if ((target % 100) == 0) {
            highlight = true
            onCount()
        } else {
            highlight = false
        }
    }

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            if (highlight) "0️⃣️️\n|" else "👇\n|",
            modifier = Modifier.graphicsLayer { translationY = -405f },
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontSize = 32.sp,
            lineHeight = 32.sp,
            fontWeight = FontWeight(200)
        )

        MetalCircle({ rotation.value }, modifier = Modifier.size(790.dp))
        Box(Modifier.clip(CircleShape).size(400.dp).background(Color.Black.copy(alpha = .1f)))
        Numbers(
            modifier = Modifier
                .graphicsLayer {
                    rotationZ = rotation.value
                }
        )
//        AnimatedVisibility(highlight, enter = fadeIn(tween(0)), exit = fadeOut(tween(0))) {
//            Box(
//                Modifier
//                    .graphicsLayer { translationY = -347f }
//                    .clip(RoundedCornerShape(4.dp))
//                    .background(Color.Red.copy(alpha = .4f))
//                    .height(40.dp)
//                    .width(24.dp)
//            )
//        }
    }
}

@Composable
private fun Numbers(modifier: Modifier = Modifier) {
    Box(
        modifier.size(300.dp),
        contentAlignment = Alignment.Center
    ) {
        (0..99).forEach {
            key(it) {
                val primary = (it % 10 == 0)
                val radius = if (primary) 340f else 345f
                Text(
                    text = if (primary) "|\n$it" else "|\n",
                    modifier = Modifier
                        .graphicsLayer {
                            val ratio: Double = it / 100.0
                            val deg = ratio * 360f
                            val rad = ratio * 2 * PI - 0.5 * PI


                            translationX = (cos(rad) * radius).toFloat()
                            translationY = (sin(rad) * radius).toFloat()
                            rotationZ = deg.toFloat()
                        },
                    textAlign = TextAlign.Center,
                    fontSize = if (primary) 32.sp else 20.sp,
                    color = Color.Black,
                    lineHeight = 32.sp,
                )
            }
        }
    }
}

// Code below based on https://www.sinasamaki.com/brushed-metal-ui-in-jetpack-compose/
@Composable
fun MetalCircle(angle: () -> Float, modifier: Modifier = Modifier) {
    Box(
        modifier
            .padding(9.dp)
            .size(128.dp)
            .graphicsLayer {
                scaleX = 0.95f
                scaleY = 0.95f
            }
            .brushedMetal(
                baseColor = Color.LightGray,
                shape = CircleShape,
                highlightAlpha = .99f,
                ringAlpha = .6f,
                highlightRotation = angle
            )
            .border(
                width = 1.dp,
                shape = CircleShape,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        Color.Transparent,
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {}
}

@Composable
fun Modifier.brushedMetal(
    baseColor: Color = Color(0xFF9A9A9A),
    shape: Shape = RectangleShape,
    ringAlpha: Float = .2f,
    ringCount: Int = 40,
    highlightAlpha: Float = .5f,
    highlightCount: Int = 3,
    highlightRotation: () -> Float = { 0f },
    center: Offset = Offset(.5f, .5f),
): Modifier {
    val highlightColor = remember(baseColor, highlightAlpha) {
        lerp(baseColor, Color.White, .5f).copy(alpha = highlightAlpha)
    }
    val ringColors = remember(ringCount) {
        val ringColor = lerp(baseColor, Color.Black, .5f).copy(alpha = ringAlpha)
        buildList {
            (0..ringCount).forEach {
                (0..Random.nextInt(2, 19)).forEach { add(Color.Transparent) }
                (0..Random.nextInt(0, 3)).forEach { add(ringColor) }
            }
        }
    }
    return this
        .drawWithCache {
            val path = Path().apply {
                addOutline(
                    shape.createOutline(size, layoutDirection, Density(density))
                )
            }
            onDrawBehind {
                clipPath(path) {
                    val center = Offset(center.x * size.width, center.y * size.height)
                    drawRect(color = baseColor)
                    drawRect(
                        brush = Brush.radialGradient(
                            colors = ringColors,
                            tileMode = TileMode.Repeated,
                            center = center,
                            radius = size.width * .2f,
                        ),
                        blendMode = BlendMode.Overlay,
                    )
                    rotate(
                        degrees = highlightRotation(),
                        pivot = center
                    ) {
                        drawCircle(
                            brush = Brush.sweepGradient(
                                colors = buildList {
                                    add(highlightColor)
                                    repeat(highlightCount) {
                                        add(highlightColor.copy(alpha = 0f))
                                        if (it < highlightCount - 1) add(highlightColor)
                                    }
                                    add(highlightColor)
                                },
                                center = center
                            ),
                            radius = size.width * size.height
                        )
                    }
                }
            }
        }
}
