/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.openwhisk.tools.github
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration._

object Main extends App {
  implicit val system: ActorSystem = ActorSystem("github-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContext = system.dispatcher

  val reporter = new GithubReporter()
  val f = reporter.generateReport()
  f.foreach(println)
  f.onComplete { _ =>
    system.terminate()
    Await.result(system.whenTerminated, 30.seconds)
  }
}