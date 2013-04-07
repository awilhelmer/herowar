editorEventbus = 
	worldAdded						: new signals.Signal()
	terrainAdded					: new signals.Signal()
	objectAdded						: new signals.Signal()
	
	keyup									: new signals.Signal()
	keydown								: new signals.Signal()
	mouseup								: new signals.Signal()
	mousedown							: new signals.Signal()
	mousemove							: new signals.Signal()
	mousewheel						: new signals.Signal()
	domMouseScroll				: new signals.Signal()
	touchstart						: new signals.Signal()
	touchend							: new signals.Signal()
	touchmove							: new signals.Signal()
	
	selectTool						: new signals.Signal()
	
	selectWorldViewport		: new signals.Signal()
	selectTerrainViewport	: new signals.Signal()
	selectObjectViewport	: new signals.Signal()
	selectObjectUI				: new signals.Signal()
	
	showWorldProperties		: new signals.Signal()
	showTerrainProperties	: new signals.Signal()
	showObjectProperties	: new signals.Signal()	
return editorEventbus