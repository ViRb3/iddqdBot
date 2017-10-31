package main

case class WadLine(a: Vertex, b: Vertex, oneSided: Boolean, sectorTag: Option[Int] = None) {
  override def toString: String = s"[Line] $a <-> $b, oneSided: $oneSided"

  def intersectsWith(that: WadLine): Boolean = {
    val denom: Double = (that.b.y - that.a.y) * (this.b.x - this.a.x) - (that.b.x - that.a.x) * (this.b.y - this.a.y)
    if (denom == 0.0) return false
    val ua: Double = ((that.b.x - that.a.x) * (this.a.y - that.a.y) - (that.b.y - that.a.y) * (this.a.x - that.a.x)) / denom
    val ub: Double = ((this.b.x - this.a.x) * (this.a.y - that.a.y) - (this.b.y - this.a.y) * (this.a.x - that.a.x)) / denom
    if (ua >= 0.0 && ua <= 1.0 && ub >= 0.0 && ub <= 1.0) return true
    false
  }
}
