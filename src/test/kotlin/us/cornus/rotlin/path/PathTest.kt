package us.cornus.rotlin.path

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import us.cornus.rotlin.push

/**
 * Created by ejw on 5/30/17.
 */
class PathTest {

    companion object

    /**
     * ........
     * A###.###
     * ..B#.#X#
     * .###.###
     * ....Z...
     */
    private var MAP48 = listOf(/* transposed */
            listOf(0, 0, 0, 0, 0),
            listOf(0, 1, 0, 1, 0),
            listOf(0, 1, 0, 1, 0),
            listOf(0, 1, 1, 1, 0),
            listOf(0, 0, 0, 0, 0),
            listOf(0, 1, 1, 1, 0),
            listOf(0, 1, 0, 1, 0),
            listOf(0, 1, 1, 1, 0)
    )

    val PASSABLE_CALLBACK_48: PathCallback = { x, y ->
        if (x < 0 || y < 0 || x >= MAP48.size || y >= MAP48[0].size) {
            false
        } else {
            (MAP48[x][y] == 0)
        }
    }

    var A = listOf(0, 1)
    var B = listOf(2, 2)
    var Z = listOf(4, 4)
    var X = listOf(6, 2)
    val PATH = ArrayList<Int>()
    val PATH_CALLBACK: PathCallback = { x: Int, y: Int -> PATH.push(x, y) != 0 }

    /*
     * . . A # . B
     *  . # # . .
     * . . # . . .
     *  # . . # .
     * X # # # Z .
     */
    var MAP6: List<List<Int?>> = listOf(/* transposed */
            listOf(0, null, 0, null, 0),
            listOf(null, 0, null, 1, null),
            listOf(0, null, 0, null, 1),
            listOf(null, 1, null, 0, null),
            listOf(0, null, 1, null, 1),
            listOf(null, 1, null, 0, null),
            listOf(1, null, 0, null, 1),
            listOf(null, 0, null, 1, null),
            listOf(0, null, 0, null, 0),
            listOf(null, 0, null, 0, null),
            listOf(0, null, 0, null, 0)
    )

    var A6 = listOf(4, 0)
    var B6 = listOf(10, 0)
    var Z6 = listOf(8, 4)
    var X6 = listOf(0, 4)

    val PASSABLE_CALLBACK_6: PathCallback = { x: Int, y: Int ->
        if (x < 0 || y < 0 || x >= MAP6.size || y >= MAP6[0].size) {
            false
        } else {
            (MAP6[x][y] == 0)
        }
    }

    var VISITS = 0
    val PASSABLE_CALLBACK_VISIT: PathCallback = { _, _ ->
        VISITS++
        true
    }

    @Before
    fun beforeEach() {
        PATH.clear()
        VISITS = 0
    }

    /* path */

/* dijkstra */

    /* 8-topology */
    @Test fun dijkstra8TopologyTest1() {
        val PATH_A = listOf(0, 1, 0, 2, 0, 3, 1, 4, 2, 4, 3, 4, 4, 4)
        val dijkstra = Dijkstra(Z[0], Z[1], PASSABLE_CALLBACK_48, PathOptions(topology = 8))

        dijkstra.compute(A[0], A[1], PATH_CALLBACK)
        assertEquals("should compute correct path A", PATH_A.toString(), PATH.toString())
    }

    @Test fun dijkstra8TopologyTest2() {
        val PATH_B = listOf(2, 2, 1, 2, 0, 3, 1, 4, 2, 4, 3, 4, 4, 4)
        val dijkstra = Dijkstra(Z[0], Z[1], PASSABLE_CALLBACK_48, PathOptions(topology = 8))

        dijkstra.compute(B[0], B[1], PATH_CALLBACK)
        assertEquals("should compute correct path B", PATH_B.toString(), PATH.toString())
    }

    @Test fun dijkstra8TopologyTest3() {
        val dijkstra = Dijkstra(Z[0], Z[1], PASSABLE_CALLBACK_48, PathOptions(topology = 8))

        dijkstra.compute(X[0], X[1], PATH_CALLBACK)
        assertEquals("should survive non-existant path X", 0, PATH.size)
    }


    /* 4-topology */
    @Test fun dijkstra4TopologyTest1() {
        val PATH_A = listOf(0, 1, 0, 2, 0, 3, 0, 4, 1, 4, 2, 4, 3, 4, 4, 4)
        val dijkstra = Dijkstra(Z[0], Z[1], PASSABLE_CALLBACK_48, PathOptions(topology = 4))

        dijkstra.compute(A[0], A[1], PATH_CALLBACK)
        assertEquals("should compute correct path A", PATH_A.toString(), PATH.toString())
    }

    @Test fun dijkstra4TopologyTest2() {
        val PATH_B = listOf(2, 2, 1, 2, 0, 2, 0, 3, 0, 4, 1, 4, 2, 4, 3, 4, 4, 4)
        val dijkstra = Dijkstra(Z[0], Z[1], PASSABLE_CALLBACK_48, PathOptions(topology = 4))

        dijkstra.compute(B[0], B[1], PATH_CALLBACK)
        assertEquals("should compute correct path B", PATH_B.toString(), PATH.toString())
    }

    @Test fun dijkstra4TopologyTest3() {
        val dijkstra = Dijkstra(Z[0], Z[1], PASSABLE_CALLBACK_48, PathOptions(topology = 4))

        dijkstra.compute(X[0], X[1], PATH_CALLBACK)
        assertEquals("should survive non-existant path X", 0, PATH.size)
    }


    /* 6-topology */
    @Test fun dijkstra6TopologyTest1() {
        val PATH_A = listOf(4, 0, 2, 0, 1, 1, 2, 2, 3, 3, 5, 3, 6, 2, 8, 2, 9, 3, 8, 4)

        val dijkstra = Dijkstra(Z6[0], Z6[1], PASSABLE_CALLBACK_6, PathOptions(topology = 6))
        dijkstra.compute(A6[0], A6[1], PATH_CALLBACK)
        assertEquals("should compute correct path A", PATH_A.toString(), PATH.toString())


    }

    @Test fun dijkstra6TopologyTest2() {
        val PATH_B = listOf(10, 0, 9, 1, 8, 2, 9, 3, 8, 4)
        val dijkstra = Dijkstra(Z6[0], Z6[1], PASSABLE_CALLBACK_6, PathOptions(topology = 6))

        dijkstra.compute(B6[0], B6[1], PATH_CALLBACK)
        assertEquals("should compute correct path B", PATH_B.toString(), PATH.toString())
    }

    @Test fun dijkstra6TopologyTest3() {
        val dijkstra = Dijkstra(Z6[0], Z6[1], PASSABLE_CALLBACK_6, PathOptions(topology = 6))

        dijkstra.compute(X6[0], X6[1], PATH_CALLBACK)
        assertEquals("should survive non-existant path X", 0, PATH.size)
    }


    /* A* */
    /* 8-topology */
    @Test fun aStar8TopologyTest1() {
        val PATH_A = listOf(0, 1, 0, 2, 0, 3, 1, 4, 2, 4, 3, 4, 4, 4)
        val astar = AStar(Z[0], Z[1], PASSABLE_CALLBACK_48, PathOptions(topology = 8))

        astar.compute(A[0], A[1], PATH_CALLBACK)
        assertEquals("should compute correct path A", PATH_A.toString(), PATH.toString())
    }

    @Test fun aStar8TopologyTest2() {
        val PATH_B = listOf(2, 2, 1, 2, 0, 3, 1, 4, 2, 4, 3, 4, 4, 4)
        val astar = AStar(Z[0], Z[1], PASSABLE_CALLBACK_48, PathOptions(topology = 8))

        astar.compute(B[0], B[1], PATH_CALLBACK)
        assertEquals("should compute correct path B", PATH_B.toString(), PATH.toString())
    }

    @Test fun aStar8TopologyTest3() {
        val astar = AStar(Z[0], Z[1], PASSABLE_CALLBACK_48, PathOptions(topology = 8))


        astar.compute(X[0], X[1], PATH_CALLBACK)
        assertEquals("should survive non-existant path X", 0, PATH.size)
    }

    @Test fun aStar8TopologyTest4() {
        val open_astar = AStar(0, 0, PASSABLE_CALLBACK_VISIT, PathOptions(topology = 8))

        open_astar.compute(50, 0, PATH_CALLBACK)
        assertEquals("should efficiently compute path", 400, VISITS)

    }

    /* 4-topology */
    @Test fun aStar4TopologyTest1() {
        val PATH_A = listOf(0, 1, 0, 2, 0, 3, 0, 4, 1, 4, 2, 4, 3, 4, 4, 4)
        val astar = AStar(Z[0], Z[1], PASSABLE_CALLBACK_48, PathOptions(topology = 4))

        astar.compute(A[0], A[1], PATH_CALLBACK)
        assertEquals("should compute correct path A", PATH_A.toString(), PATH.toString())
    }

    @Test fun aStar4TopologyTest2() {
        val PATH_B = listOf(2, 2, 1, 2, 0, 2, 0, 3, 0, 4, 1, 4, 2, 4, 3, 4, 4, 4)
        val astar = AStar(Z[0], Z[1], PASSABLE_CALLBACK_48, PathOptions(topology = 4))

        astar.compute(B[0], B[1], PATH_CALLBACK)
        assertEquals("should compute correct path B", PATH_B.toString(), PATH.toString())
    }

    @Test fun aStar4TopologyTest3() {
        val astar = AStar(Z[0], Z[1], PASSABLE_CALLBACK_48, PathOptions(topology = 4))


        astar.compute(X[0], X[1], PATH_CALLBACK)
        assertEquals("should survive non-existant path X", 0, PATH.size)
    }


    /* 6-topology */
    @Test fun aStarTest6Topology1() {
        val PATH_A = listOf(4, 0, 2, 0, 1, 1, 2, 2, 3, 3, 5, 3, 6, 2, 8, 2, 9, 3, 8, 4)
        val astar = AStar(Z6[0], Z6[1], PASSABLE_CALLBACK_6, PathOptions(topology = 6))


        astar.compute(A6[0], A6[1], PATH_CALLBACK)
        assertEquals("should compute correct path A", PATH_A.toString(), PATH.toString())
    }

    @Test fun aStarTest6Topolog2y() {
        val PATH_B = listOf(10, 0, 9, 1, 8, 2, 9, 3, 8, 4)
        val astar = AStar(Z6[0], Z6[1], PASSABLE_CALLBACK_6, PathOptions(topology = 6))

        astar.compute(B6[0], B6[1], PATH_CALLBACK)
        assertEquals("should compute correct path B", PATH_B.toString(), PATH.toString())
    }

    @Test fun aStarTest6Topology3() {
        val astar = AStar(Z6[0], Z6[1], PASSABLE_CALLBACK_6, PathOptions(topology = 6))

        astar.compute(X6[0], X6[1], PATH_CALLBACK)
        assertEquals("should survive non-existant path X", 0, PATH.size)
    }


}
