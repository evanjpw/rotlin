package us.cornus.rotlin

import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.junit.runners.Suite.SuiteClasses
import us.cornus.rotlin.path.PathTest
import us.cornus.rotlin.noise.SimplexTest

@RunWith(Suite::class)
@SuiteClasses(StringGeneratorTest::class, RNGTest::class, ExtensionsTest::class, EventQueueTest::class, ROTTextTest::class, PathTest::class, SimplexTest::class)
class AllTests
