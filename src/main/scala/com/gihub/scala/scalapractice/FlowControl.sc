// If /Else statement
if (1 > 3) println("its true") else println("Its false")

//if-else block
if (1 > 3) {
  println("Is is true!")
  println("Really?")
} else {
  println("No its false")
  println("still.")
}

//match--like switch in scala
val number = 2
number match {
  case 1 => println("One")
  case 2 => println("Two")
  case 3 => println("Three")
  case _ => println("Something else")
}

//for loop
for (x <- 1 to 4) {
  println(x)
}

//while
var x = 1
while (x < 10) {
  println(x)
  x += 1
}

// Expressions
{
  val x = 10; x + 20
} //The last statement is the return statement.

println({
  val x = 10; x + 20
})