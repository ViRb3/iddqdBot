package main

case class Lump(name: String, data: List[Byte]) {
  override def toString: String = "[Lump] name: " + name
}
