package agents

import akka.actor.ActorSystem
import akka.agent.Agent
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


/**
  * Agents are instance linked with a resource, and only this agent it´s able to mutate the resource
  * The api of the agent allow us to update the resource sync and async
  */
object Agents extends App {

  implicit val context = ActorSystem()
  implicit val materializer = ActorMaterializer()

  runAgent()

  def runAgent() {
    val agent = Agent(5)
    println(s"Read agent value:${agent.get()}")
    val update: Future[Int] = agent.alter(10)
    update.onComplete(value => println(s"Agent update value:${value.get}"))
    agent.send(value => value + 100)
    println(s"Async update not guaranteed:${agent.get()}")
    agent.future().onComplete(value => println(s"Wait for data updated:${value.get}"))
  }

}
