package us.cornus.rotlin

import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.junit.runners.Suite.SuiteClasses

@RunWith(Suite::class)
@SuiteClasses(StringGeneratorTest::class, RNGTest::class)
class AllTests
