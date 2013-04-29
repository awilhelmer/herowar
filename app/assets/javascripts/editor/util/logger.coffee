Log = require 'models/ui/log'
db = require 'database'

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
	logs = db.get 'ui/logs'
	item = new Log()
	item.set
		'level' : level
		'message'	: message
		'cdate' : new Date().getTime()
	logs.add item
	
return logger