package net.barribob.maelstrom.general.io

interface IFileManager {
    fun mkdirs(filePath: String): Boolean
    fun exists(filePath: String): Boolean
    fun copy(fromFilePath: String, toFilePath: String)
    fun copyFromSrc(fromFilePath: String, toFilePath: String)
}