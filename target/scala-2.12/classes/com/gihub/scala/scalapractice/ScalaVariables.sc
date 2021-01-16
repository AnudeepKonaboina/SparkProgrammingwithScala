//VAL --immutable variables like final in java
val hello: String = "Hello"
//hello = hello + " Mark!!" //Error reassignment to val hello= hello+" Mark!!"

val newhello: String = hello + " Mark!!"
println(newhello)

//VAR --mutable data
var helloworld: String = "Helloworld"
helloworld = helloworld + " Mark!!" //this is possible
println(helloworld)

// Data Types
val numberOne: Int = 1
val truth: Boolean = true
val letterA: Char = 'a'
val pi: Double = 3.14159265
val piSinglePrecision: Float = 3.14159265f
val bigNumber: Long = 123456789
val smallNumber: Byte = 127

println("Concat output : " + numberOne + truth + letterA + pi + bigNumber)
print(f"printing a number $numberOne%.0f") //for formatting datat
print(s"printing a number $numberOne") //substitute a value
println(s"The s prefix isn't limited to variables; I can include any expression. Like ${1 + 2}") //print an expression

//Pattern matching
val stringMatch: String = "My age is 18."
val pattern = """.* ([\d]+).*""".r
val pattern(result)=stringMatch
print(result)

//string match
val picard: String = "Peter"
val bestCaptain: String = "Peter"
val isBest: Boolean = picard == bestCaptain
