BaseView = require 'views/baseView'
events = require 'events'

class BaseStatsView extends BaseView

	className: 'item'

	statsName: null

	isChanging: false

	bindEvents: ->
		events.on "stats:#{@statsName}:changed", @onValueChange, @ if @statsName
		
	onValueChange: (changed) ->
		return if @isChanging
		@isChanging = true
		spans = @$ '.update'
		spans.remove() if spans.length isnt 0
		type = if changed.indexOf('+') is 0 then 'positive' else 'negative'
		@$el.append "<span class=\"update #{type}\">#{changed}</span>"
		setTimeout () =>
			@$('.update').addClass 'invisible'
			@isChanging = false
		, 1

return BaseStatsView