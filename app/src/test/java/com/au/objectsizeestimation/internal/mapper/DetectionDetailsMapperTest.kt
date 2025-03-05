package com.au.objectsizeestimation.internal.mapper

import android.graphics.RectF
import com.au.objectsizeestimation.internal.ui.Classification
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.task.vision.detector.Detection

class DetectionDetailsMapperTest {

    private val detectionDetailsMapper = DetectionDetailsMapper()

    @Test
    fun `create - detection list with category score of less than cutoff score - returns correct results`() {
        val category1 = mock<Category> {
            on { label } doReturn "label"
            on { score } doReturn 0.4f
        }
        val category2 = mock<Category> {
            on { label } doReturn "label"
            on { score } doReturn 0.3f
        }

        val output = detectionDetailsMapper.create(
            listOf(
                mock<Detection> {
                    on { categories } doReturn listOf(category1)
                },
                mock<Detection> {
                    on { categories } doReturn listOf(category2)
                }
            )
        )

        assertThat(output).isEmpty()
    }

    @Test
    fun `create - detection list with category score of more than cutoff score - returns correct results`() {
        val category1 = mock<Category> {
            on { label } doReturn "label"
            on { score } doReturn 0.5f
        }
        val category2 = mock<Category> {
            on { label } doReturn "label"
            on { score } doReturn 0.6f
        }
        val boundingBox1 = spy(RectF(1f, 2f, 3f, 4f))
        val boundingBox2 = spy(RectF(5f, 6f, 7f, 4f))

        val output = detectionDetailsMapper.create(
            listOf(
                mock<Detection> {
                    on { boundingBox } doReturn boundingBox1
                    on { categories } doReturn listOf(category1)
                },
                mock<Detection> {
                    on { boundingBox } doReturn boundingBox2
                    on { categories } doReturn listOf(category2)
                }
            )
        )

        assertThat(output).isNotEmpty()

        assertThat(output).isEqualTo(
            listOf(
                Classification(
                    label = "label",
                    score = 0.5f,
                    boundingBox = boundingBox1
                ),
                Classification(
                    label = "label",
                    score = 0.6f,
                    boundingBox = boundingBox2
                )
            )
        )
    }
}