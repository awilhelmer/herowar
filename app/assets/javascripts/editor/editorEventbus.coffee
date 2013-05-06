log = require 'util/logger'

editorEventbus = 

	dispatch: (name, params) ->
		if arguments?.length > 0
			args = [].slice.call arguments
			name = args.shift()
			unless args.length isnt 0
				log.debug "[EVENT] Dispatch \"#{name}\""
			else
				log.debug "[EVENT] Dispatch \"#{name}\" with parameters \"#{args.join(',')}\""
			@[name].dispatch.apply null, args

	render									: new signals.Signal()
	toggleTab								: new signals.Signal()

	newMapEmpty							: new signals.Signal()

	keyup									 	: new signals.Signal()
	keydown								 	: new signals.Signal()
	mouseup									: new signals.Signal()
	mousedown								: new signals.Signal()
	mousemove							 	: new signals.Signal()
	mousewheel							: new signals.Signal()
	domMouseScroll					: new signals.Signal()
	touchstart							: new signals.Signal()
	touchend								: new signals.Signal()
	touchmove								: new signals.Signal()

	selectBrush							: new signals.Signal()
	selectBrushSize					: new signals.Signal()

	selectWorldViewport		 	: new signals.Signal()
	selectTerrainViewport		: new signals.Signal()
	selectObjectViewport		: new signals.Signal()
	selectWorldUI					 	: new signals.Signal()
	selectTerrainUI				 	: new signals.Signal()
	selectObjectUI					: new signals.Signal()
	selectPathUI						: new signals.Signal()
	selectWaveUI						: new signals.Signal()
	removeStaticObject			: new signals.Signal()

	selectMaterial					: new signals.Signal()
	updateModelMaterial			: new signals.Signal()
	changeMaterial					: new signals.Signal()

	changeTerrain					 	: new signals.Signal()
	changeTerrainWireframe	: new signals.Signal()
	resetTerrainPool				: new signals.Signal()
	resetWireframe					: new signals.Signal()

	changeStaticObject			: new signals.Signal()

	treeLoadData						: new signals.Signal()
	treeSelectItem					: new signals.Signal()
	listSelectItem					: new signals.Signal()
	listSetItem							: new signals.Signal()
	
	initIdChanged						: new signals.Signal()

	handleWorldMaterials		: new signals.Signal()

return editorEventbus