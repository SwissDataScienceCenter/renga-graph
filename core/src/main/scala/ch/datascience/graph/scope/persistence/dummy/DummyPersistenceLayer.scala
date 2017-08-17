package ch.datascience.graph.scope.persistence.dummy

import ch.datascience.graph.scope.persistence.PersistenceLayer

class DummyPersistenceLayer()
  extends PersistenceLayer
  with DummyPersistedProperties
  with DummyPersistedNamedTypes
