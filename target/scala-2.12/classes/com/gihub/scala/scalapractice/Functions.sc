// Functions
//Format def <function name>(parameter name: type...) : return type = { }
def myfunc(x: Int): Int = {
  x * x
}
myfunc(5)

def transformInt(x: Int, f: Int => Int): Int = {
  f(x)
}

val res = transformInt(2, myfunc)
print(res)

def convertCase(str: String): String = {
  str.toUpperCase
}
print(convertCase("hi"))


