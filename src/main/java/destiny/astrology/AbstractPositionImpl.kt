/**
 * Created by smallufo on 2017-07-02.
 */
package destiny.astrology

import java.io.Serializable

abstract class AbstractPositionImpl<out T : Point>
  internal constructor(override val point: T) : IPosition<T>, Serializable
