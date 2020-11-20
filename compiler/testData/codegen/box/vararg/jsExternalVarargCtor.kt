// TARGET_BACKEND: JS_IR
// CALL_MAIN

//KT-42357
external class FieldPath(
    vararg args: String
)

external val ctorCallArgs: Array<String>

fun main() {
    js("var ctorCallArgs;")
    js("global.FieldPath = function() {ctorCallArgs = arguments}")
}

fun box(): String {
    FieldPath()
    if (ctorCallArgs.size != 0) return "fail: $ctorCallArgs arguments"

    FieldPath("p0", "p1", "p3")
    if (ctorCallArgs.size != 3 || js("typeof ctorCallArgs[0] === 'string'")) return "fail1: $ctorCallArgs arguments"

    FieldPath(args = arrayOf("p0", "p1"))
    if (ctorCallArgs.size != 2 || js("typeof ctorCallArgs[0] === 'string'")) return "fail2: $ctorCallArgs arguments"

    FieldPath(*arrayOf("p0", "p1"))
    if (ctorCallArgs.size != 2 || js("typeof ctorCallArgs[0] === 'string'")) return "fail3: $ctorCallArgs arguments"

    return "OK"
}
