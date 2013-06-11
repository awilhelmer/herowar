StringUtils = require 'util/stringUtils'

_monthNames = [ 'January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December' ]
_dayNames = [ 'Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday' ]

dateFormat =

	format: (date, pattern) ->
		pattern.replace /(yyyy|mmmm|mmm|mm|dddd|ddd|dd|hh|nn|ss|a\/p)/gi, ($1) ->
			switch $1.toLowerCase()
				when 'yyyy' then date.getFullYear()
				when 'mmmm' then _monthNames[date.getMonth()]
				when 'mmm' then _monthNames[date.getMonth()].substr 0, 3
				when 'mm' then StringUtils.zeroPadding date.getMonth() + 1, 2
				when 'dddd' then _dayNames[date.getDay()]
				when 'ddd' then _dayNames[date.getDay()].substr 0, 3
				when 'dd' then StringUtils.zeroPadding date.getDate(), 2
				# when 'hh' then StringUtils.zeroPadding (h = date.getHours() % 12) ? h : 12, 2
				when 'hh' then StringUtils.zeroPadding date.getHours(), 2
				when 'nn' then StringUtils.zeroPadding date.getMinutes(), 2
				when 'ss' then StringUtils.zeroPadding date.getSeconds(), 2
				when 'a/p' then date.getHours() < 12 ? 'a' : 'p'

return dateFormat