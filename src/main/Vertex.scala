package main

case class Vertex(x: Int, y: Int) {
  override def toString: String = s"($x, $y)"

  def +(that: Vertex): Vertex = {
    Vertex(x + that.x, y + that.y)
  }
}
