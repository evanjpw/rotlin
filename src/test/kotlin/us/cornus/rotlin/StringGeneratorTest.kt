package us.cornus.rotlin

import org.junit.Assert.*
import org.junit.Test
import java.util.logging.Level
import java.util.logging.Logger

/**
 * Created by ejw on 5/28/17.
 */
class StringGeneratorTest {
    val logger = Logger.getLogger(StringGeneratorTest::class.java.simpleName)

    @Test
    fun clearTest() {
        val options = StringGeneratorOptions()
        val generator = StringGenerator(options)
        val gs1 = generator.getStats()
        assertEquals(EMPTY_STATS, gs1)
        train(generator, TRAINING_SET1)
        generator.clear()
        val gs2 = generator.getStats()
        assertEquals(EMPTY_STATS, gs2)
    }

    @Test
    fun generateTest() {
        val generator = StringGenerator()
        val gs1 = generator.getStats()
        assertEquals(EMPTY_STATS, gs1)
        train(generator, TRAINING_SET1)
        val text = generator.generate()
        logger.log(Level.INFO, "generate(): Text was \"$text\".")
        assertFalse(text.isNullOrEmpty())
    }

    @Test
    fun observeTest() {
        val generator = StringGenerator()
        val gs1 = generator.getStats()
        assertEquals(EMPTY_STATS, gs1)
        train(generator, TRAINING_SET1)
        val gs2 = generator.getStats()
        assertNotEquals(EMPTY_STATS, gs2)
    }

    @Test
    fun getStatsTest() {
        val generator = StringGenerator()
        val gs1 = generator.getStats()
        assertEquals(EMPTY_STATS, gs1)
        train(generator, TRAINING_SET1)
        val gs2 = generator.getStats()
        assertEquals(TRAINING_SET_1_STATS, gs2)
    }

    private fun train(generator : StringGenerator, trainingSet : List<String>) {
        for(observable in trainingSet) {
            generator.observe(observable)
        }
    }

    companion object {
        val EMPTY_STATS = "distinct samples: 0, dictionary size (contexts): 0, dictionary size (events): 0"
        val TRAINING_SET1 = listOf<String>(
                "Land of the glass pinecones",
                "They only grow for the full moon",
                "The farmers never gather them",
                "The magic cones are heaven-sent",
                "Land of the glass pinecones",
                "Their seeds are made of rhinestones",
                "The squirrels never scatter them",
                "They know what rhinestone seeds portend",
                "Land of the glass pinecones",
                "They smash on the grass when the wind blows",
                "The splinters fly throughout the land",
                "And pierce the eye of every man",
                "Land of the glass pinecones",
                "The eye now sees what the tree knows",
                "The splinters burn, but then we learn",
                "That when we spend we have to bend",
                "It's all a part of nature's plan",
                "All a part of nature's plan",
                "Land of the glass pinecones",
                "My mind is golden inside",
                "My mind is golden inside",
                "My mind is golden inside",
                "My mind is golden inside"
        )
        val TRAINING_SET_1_STATS = "distinct samples: 33, dictionary size (contexts): 895, dictionary size (events): 921"
    }
}