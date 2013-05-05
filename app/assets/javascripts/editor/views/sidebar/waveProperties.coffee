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
		'shown a[data-toggle="tab"]'				: 'changeTab'
		'change input[name="name"]'					: 'onChangedString'
		'change input[name="prepareTime"]'	: 'onChangedInteger'
		'change input[name="waveTime"]'			: 'onChangedInteger'
		'change input[name="quantity"]'			: 'onChangedInteger'
		'change select[name="path"]'				: 'onChangedInteger'

	initialize: (options) ->
		@tabs = db.get 'ui/tabs'
		@tabs.set 'waveProperties', '#wp-basis'
		@unitsLoaded = false
		super options

	bindEvents: ->
		EditorEventbus.selectWaveUI.add @selectWave
		EditorEventbus.listSelectItem.add @listSelectItem

	changeTab: (event) ->
		unless event then return
		$currentTarget = $ event.target
		tab = $currentTarget.data 'target'
		@tabs.set 'waveProperties', tab
		if tab is '#wp-unit' and not @unitsLoaded
			@unitsLoaded = true
			units = db.get 'units'
			units.fetch()

	selectWave: (value) =>
		@model = db.get 'waves', value
		@render()

	listSelectItem: (id, value, name) =>
		@model.set 'unit', value if id is 'sidebar-properties-wave-unit-list'

	getTemplateData: ->
		json = super()
		json.paths = @getPathsFromDB() if @model
		json.isActiveTabBasis = @tabs.get('waveProperties') is '#wp-basis'
		json.isActiveTabUnit = @tabs.get('waveProperties') is '#wp-unit'
		json

	getPathsFromDB: ->
		paths = db.get('paths').toJSON()
		for path in paths
			if path.id is @model.get('path') then path.selected = true else path.selected = false
		paths

return WaveProperties