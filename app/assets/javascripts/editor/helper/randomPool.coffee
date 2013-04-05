class RandomPool
	
	origRandom: Math.random
	pool: []
	counter: 0
	
	next: =>
		if @counter >= @pool.length
			rand = @origRandom()
			@pool.push(rand)
		else
			rand = @pool[@counter]
		@counter++
		rand

	seek: (index) ->
		@counter = Math.min index, @pool.length

	reset: ->
		@counter = 0
		@pool = []

	hook: ->
		Math.random = @next

	unhook: ->
		Math.random = @origRandom

return RandomPool