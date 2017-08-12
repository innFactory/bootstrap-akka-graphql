package de.innfactory.models

import com.byteslounge.slickrepo.meta.Entity
import sangria.macros.derive.{GraphQLField, _}

@GraphQLDescription(description = "Thats a dummy! Get id or a string...")
case class Dummy(@GraphQLField override val id: Option[Long],
                 @GraphQLField @GraphQLDeprecated(deprecationReason = "dummy val is old") dummy: String) extends Entity[Dummy, Long] {
  override def withId(id: Long): Dummy = this.copy(id = Some(id))
}
