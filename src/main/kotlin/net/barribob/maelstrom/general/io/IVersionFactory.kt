package net.barribob.maelstrom.general.io

interface IVersionFactory <T : IVersion> {
    fun loadFromSrc(baseResourceName: String): T
    fun loadFromRun(pathName: String): T
}