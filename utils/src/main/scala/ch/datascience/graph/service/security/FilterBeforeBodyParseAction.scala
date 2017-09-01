package ch.datascience.graph.service.security

import akka.util.ByteString
import play.api.libs.streams.Accumulator
import play.api.mvc._

import scala.concurrent.Future

/**
 * Created by johann on 11/07/17.
 */
trait AbstractFilterBeforeBodyParseAction extends ActionBuilder[Request] {

  protected def filter( rh: RequestHeader ): Either[Result, RequestHeader]

  override protected def composeAction[A]( action: Action[A] ): Action[A] = new Action[A] {

    override def apply( rh: RequestHeader ): Accumulator[ByteString, Result] = filter( rh ) match {
      case Left( result ) => Accumulator.done( result )
      case Right( newRH ) => action.apply( newRH )
    }

    def apply( request: Request[A] ): Future[Result] = action.apply( request )

    def parser: BodyParser[A] = action.parser

  }

  private[this] def makeError[A]( result: Result ): BodyParser[A] = BodyParsers.parse.error( Future.successful( result ) )

}

case class FilterBeforeBodyParseAction( filter: ( RequestHeader ) => Either[Result, RequestHeader] ) extends AbstractFilterBeforeBodyParseAction {

  protected def filter( rh: RequestHeader ): Either[Result, RequestHeader] = filter.apply( rh )

  def invokeBlock[A]( request: Request[A], block: ( Request[A] ) => Future[Result] ): Future[Result] = block( request )

}
