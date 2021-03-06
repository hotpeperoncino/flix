/*
 * Copyright 2016 Magnus Madsen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ca.uwaterloo.flix.library

import ca.uwaterloo.flix.util.FlixTest
import org.scalatest.{ParallelTestExecution, Suites}

class LibrarySuite extends Suites(
  new FlixTest("TestPrelude", "main/test/ca/uwaterloo/flix/library/TestPrelude.flix"),
  new FlixTest("TestInt8", "main/test/ca/uwaterloo/flix/library/TestInt8.flix"),
  new FlixTest("TestInt16", "main/test/ca/uwaterloo/flix/library/TestInt16.flix"),
  new FlixTest("TestInt32", "main/test/ca/uwaterloo/flix/library/TestInt32.flix"),
  new FlixTest("TestInt64", "main/test/ca/uwaterloo/flix/library/TestInt64.flix"),
  new FlixTest("TestBigInt", "main/test/ca/uwaterloo/flix/library/TestBigInt.flix"),
  new FlixTest("TestFloat32", "main/test/ca/uwaterloo/flix/library/TestFloat32.flix"),
  new FlixTest("TestFloat64", "main/test/ca/uwaterloo/flix/library/TestFloat64.flix"),
  new FlixTest("TestOption", "main/test/ca/uwaterloo/flix/library/TestOption.flix"),
  new FlixTest("TestResult", "main/test/ca/uwaterloo/flix/library/TestResult.flix"),
  new FlixTest("TestList", "main/test/ca/uwaterloo/flix/library/TestList.flix"),
  new FlixTest("TestSet", "main/test/ca/uwaterloo/flix/library/TestSet.flix"),
  new FlixTest("TestMap", "main/test/ca/uwaterloo/flix/library/TestMap.flix")
) with ParallelTestExecution {
  /* left empty */
}
