package de.innfactory

import akka.http.scaladsl.testkit.ScalatestRouteTest
import de.innfactory.graphql.GraphqlSchemaDefinition.apiSchema
import de.innfactory.graphql.{GraphQLContext, GraphQLContextServices}
import de.innfactory.http.HttpService
import de.innfactory.services.{AuthService, DummyService}
import de.innfactory.utils.AutoValidate
import de.innfactory.utils.InMemoryPostgresStorage._
import org.scalatest.{Matchers, WordSpec}
import sangria.ast.Document
import sangria.execution.Executor
import sangria.marshalling.sprayJson._
import spray.json._

import scala.concurrent.Await
import scala.concurrent.duration._

trait BaseServiceTest extends WordSpec with Matchers with ScalatestRouteTest {


  dbProcess.getProcessId

  val dummyService = new DummyService()
  implicit val authService = new AuthService(new AutoValidate)
  val graphQLContextServices = GraphQLContextServices(authService, dummyService)

  val httpService = new HttpService(graphQLContextServices)

  def executeQuery(query: Document, vars: JsObject = JsObject.empty) = {
    val futureResult = Executor.execute(apiSchema, query,
      variables = vars,
      userContext = GraphQLContext(None, graphQLContextServices)
      )
    Await.result(futureResult, 10.seconds)
  }
}
