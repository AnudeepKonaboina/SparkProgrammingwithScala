// Data structures

// Tuples
// Immutable lists
val fruits = ("Apple", "Orange", "Pineapple")
println(fruits)

val captainStuff = ("Picard", "Enterprise-D", "NCC-1701-D")
println(captainStuff)

// Refer to the individual fields with a ONE-BASED index
println(fruits._1) //tuple starts form index 1
println(captainStuff._2)
println(captainStuff._3)

val map = "fruit" -> "orange"
println(map._2)

val aBunchOfStuff = ("Kirk", 1964, true) //tuple with different data types

//Lists
//More functionality and all the elements must be of same type

val fruitsList = List("apple", "mango", "orange")
println(fruitsList(1)) //list starts form zero
//for loop
for (fruit <- fruitsList) {
  println(fruit)
}
//for each
fruitsList.foreach(fruit => {
  println(fruit)
})

//user of map
val backwordfruits = fruitsList.map((fruit: String) => {
  fruit.reverse
})
for (fruit <- backwordfruits) {
  println(fruit)
}

//use of reduce:to combine together all the items in a collection using some function
val numberList = List(1, 2, 3, 4, 5)
val sum = numberList.reduce((x: Int, y: Int) => x + y)
println(sum)

// filter() removes stuff
val iHateFives = numberList.filter((x: Int) => x != 5)
val iHateThrees = numberList.filter(_ != 3)

// MAPS
var shipMap = Map("Kirk" -> "Enterprise", "Picard" -> "Enterprise-D", "Sisko" -> "Deep Space Nine", "Janeway" -> "Voyager")
println(shipMap("Janeway"))
println(shipMap.contains("Archer"))
val archersShip = util.Try(shipMap("Archer")) getOrElse "Unknown"
println(archersShip)
