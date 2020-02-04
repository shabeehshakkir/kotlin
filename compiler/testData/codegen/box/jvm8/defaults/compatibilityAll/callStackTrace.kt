// !JVM_DEFAULT_MODE: all
// IGNORE_BACKEND_FIR: JVM_IR
// TARGET_BACKEND: JVM
// FULL_JDK
// WITH_RUNTIME
// FILE: Simple.java

public interface Simple extends KInterface {
    default java.util.List<String> compatibilityCall() {
        return KInterface.DefaultImpls.call(this);
    }
}

// FILE: Foo.java
public class Foo implements Simple {

}

// FILE: main.kt
// JVM_TARGET: 1.8
// WITH_RUNTIME

interface KInterface  {
    fun call(): List<String> {
        return Thread.currentThread().getStackTrace().map { it.className + "." + it.methodName }
    }
}


fun box(): String {
    var result = Foo().compatibilityCall()
    if (result[1] != "KInterface.call") return "fail 1: ${result[1]}"
    if (result[2] != "KInterface.access\$call\$jd") return "fail 2: ${result[2]}"
    if (result[3] != "KInterface\$DefaultImpls.call") return "fail 3: ${result[3]}"
    if (result[4] != "Simple.compatibilityCall") return "fail 4: ${result[4]}"
    if (result[5] != "MainKt.box") return "fail 5: ${result[5]}"

    result = Foo().call()
    if (result[1] != "KInterface.call") return "fail 1: ${result[1]}"
    if (result[2] != "MainKt.box") return "fail 2: ${result[2]}"

    return "OK"
}
