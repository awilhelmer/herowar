Log = require 'models/ui/log'
db = require 'database'
DateFormat = require 'util/dateFormat'

logger =
	
	debug: (message) ->
		log "DEBUG", message
	
	info: (message) ->
		log "INFO", message

	warn: (message) ->
		log "WARN", message
	
	error: (message) ->
		log "ERROR", message

log = (level, message) ->
	curDate = new Date()
	logs = db.get 'ui/logs'
	item = new Log()
	item.set
		'level' : level
		'message'	: message
		'cdate' : curDate.getTime()
		'formattedDate' : DateFormat.format curDate, 'ddd mmm dd hh:nn:ss'
	logs.add item
	
return logger