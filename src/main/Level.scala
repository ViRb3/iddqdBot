package main

case class Level(name: String, lumps: Map[String, Lump], lines: Option[List[WadLine]] = None, var playerStart: Option[Vertex] = None,
                 var sectors: Option[List[Sector]] = None, var things: List[Thing] = null, var rawSidedefs: List[List[Byte]] = null, var rawSectors: List[List[Byte]] = null){

  def addLump(lump: Lump): Level = {
    val newLumps: Map[String, Lump] = lumps + (lump.name -> lump)
    Level(this.name, newLumps, this.lines, this.playerStart, this.sectors, this.things, this.rawSidedefs, this.rawSectors)
  }

  def setLines(lines: List[WadLine]): Level =
    Level(this.name, this.lumps, Some(lines), this.playerStart, this.sectors, this.things, this.rawSidedefs, this.rawSectors)

  def setSectors(sectors: List[Sector]): Level =
    Level(this.name, this.lumps, this.lines, this.playerStart, Some(sectors), this.things, this.rawSidedefs, this.rawSectors)

  def setPlayerStart(v: Vertex): Unit = this.playerStart = Some(v)

  override def toString: String = "[Level] name: " + name
}
