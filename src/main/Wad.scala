package main

case class Wad(wadType: String, numLumps: Int, levels: List[Level]) {
  override def toString: String = "[Wad] type: " + wadType + ", lumps: " + numLumps + ", levels: " + levels
}
