editorEventbus = 
		worldAdded							: new signals.Signal()
		terrainAdded						: new signals.Signal()
		objectAdded						 	: new signals.Signal()
	
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
	
		selectTool							: new signals.Signal()
		selectBrush							: new signals.Signal()
		selectBrushSize					: new signals.Signal()
	
		selectWorldViewport		 	: new signals.Signal()
		selectTerrainViewport		: new signals.Signal()
		selectObjectViewport		: new signals.Signal()
		selectWorldUI					 	: new signals.Signal()
		selectTerrainUI				 	: new signals.Signal()
		selectObjectUI					: new signals.Signal()
	
		showWorldProperties		 	: new signals.Signal()
		showTerrainProperties	 	: new signals.Signal()
		showObjectProperties		: new signals.Signal()
		showMaterialProperties	: new signals.Signal()

		menuSelectMaterial			: new signals.Signal()
		selectMaterial					: new signals.Signal()
		updateModelMaterial			: new signals.Signal()
		deselectMaterial				: new signals.Signal()
		changeMaterial					: new signals.Signal()
	
		changeTerrain					 	: new signals.Signal()
		changeTerrainWireframe	: new signals.Signal()
		resetTerrainPool				: new signals.Signal()
		resetWireframe					: new signals.Signal()
return editorEventbus