package ch.datascience.graph.scope.persistence.dummy

import ch.datascience.graph.scope.persistence.PersistedNamedTypes
import ch.datascience.graph.types.NamedType

import scala.concurrent.Future

trait DummyPersistedNamedTypes extends PersistedNamedTypes {

  /**
   * Fetches named type with specified key
   *
   * @param typeId
   * @return a future containing some named type if a corresponding one is found, None otherwise
   */
  def fetchNamedTypeFor( typeId: NamedType#TypeId ): Future[Option[NamedType]] = Future.successful( None )

  /**
   * Grouped version of fetchNamedTypeFor
   *
   * If some keys are not found, they will not be part of the result map
   *
   * @param typeIds set of keys to retrieve
   * @return map key -> named type, will not contain unknown keys
   */
  def fetchNamedTypesFor( typeIds: Set[NamedType#TypeId] ): Future[Map[NamedType#TypeId, NamedType]] = Future.successful( Map.empty )

}
