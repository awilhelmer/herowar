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
	log = new Log()
	log.set
		'level' : level
		'message'	: message
		'cdate' : new Date().getTime()
	logs.add log
	
return logger