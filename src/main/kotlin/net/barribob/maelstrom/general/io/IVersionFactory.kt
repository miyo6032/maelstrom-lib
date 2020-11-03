package net.barribob.maelstrom.general.io

interface IVersionFactory {
    fun loadFromSrc(baseResourceName: String): IVersion
    fun loadFromRun(pathName: String): IVersion
}