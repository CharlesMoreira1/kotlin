// TARGET_BACKEND: JS_IR
// CALL_MAIN

//KT-42357
external fun create(
    vararg args: String
) : Array<String>

fun main() {
    js("global.create = function() {return arguments}")
}

fun box(): String {
    val zeroArgs = create()
    if (zeroArgs.size != 0) return "fail: $zeroArgs arguments"

    val varArgs = create("p0", "p1", "p3")
    if (varArgs.size != 3 || js("typeof varArgs[0] === 'string'")) return "fail1: $varArgs arguments"

    val namedParameter = create(args = arrayOf("p0", "p1"))
    if (namedParameter.size != 2 || js("typeof namedParameter[0] === 'string'")) return "fail2: $namedParameter arguments"

    val spreadArgs = create(*arrayOf("p0", "p1"))
    if (spreadArgs.size != 2 || js("typeof spreadArgs[0] === 'string'")) return "fail3: $spreadArgs arguments"

    return "OK"
}
