class Scene extends Backbone.Model

	initialize: (options) ->
		@set
			currentId      : 1
			dynamicObjects : {}
			staticObjects  : {}
			scenes         : {}

return Scene