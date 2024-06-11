var a = 1
var n = 0
for(i <- (2 to 1000).toList){
	for(d <- (1 to i / 2)){
		if( i % d == 0) a *= d
	}
	if(a == i) {
		print(i + " ")
		n += 1
	}
	a = 1
}
println()
println("doskonałych " + n)
