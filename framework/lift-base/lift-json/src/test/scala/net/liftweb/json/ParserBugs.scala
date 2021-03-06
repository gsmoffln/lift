/*
 * Copyright 2009-2010 WorldWide Conferencing, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.liftweb
package json

import org.specs.Specification
import org.specs.runner.{Runner, JUnit}
import scala.util.control.Exception._

class ParserBugsTest extends Runner(ParserBugs) with JUnit
object ParserBugs extends Specification {
  "Unicode ffff is a valid char in string literal" in {
    parseOpt(""" {"x":"\uffff"} """).isDefined mustEqual true
  }

  "Does not hang when parsing 2.2250738585072012e-308" in {
    allCatch.opt(parse(""" [ 2.2250738585072012e-308 ] """)) mustEqual None
    allCatch.opt(parse(""" [ 22.250738585072012e-309 ] """)) mustEqual None
  }
}
