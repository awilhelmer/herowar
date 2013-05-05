EditorEventbus = require 'editorEventbus'
BasePropertiesView = require 'views/basePropertiesView'
templates = require 'templates'
Constants = require 'constants'
log = require 'util/logger'
db = require 'database'

class WaveProperties extends BasePropertiesView
	
	id: 'sidebar-properties-wave'
	
	className: 'sidebar-panel hidden'
	
	template: templates.get 'sidebar/waveProperties.tmpl'

	events:
		'change input[name="name"]'					: 'onChangedString'
		'change input[name="prepareTime"]'	: 'onChangedInteger'
		'change input[name="waveTime"]'			: 'onChangedInteger'
		'change input[name="quantity"]'			: 'onChangedInteger'
		'change select[name="path"]'				: 'onChangedInteger'

	bindEvents: ->
		EditorEventbus.selectWaveUI.add @selectItem

	selectItem: (value) =>
		@model = db.get 'waves', value
		@render()

	getTemplateData: ->
		json = super()
		json.paths = @getPathsFromDB() if @model
		json

	getPathsFromDB: ->
		paths = db.get('paths').toJSON()
		for path in paths
			if path.id is @model.get('path') then path.selected = true else path.selected = false
		paths

return WaveProperties