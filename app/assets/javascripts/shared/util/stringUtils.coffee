stringUtils =

	repeat: (value, count) ->
		s = ''
		s += value for i in [0..(count - 1)] if count isnt 0
		s

	zeroPadding: (value, count) -> 
		@repeat('0', count - value.toString().length) + value

return stringUtils