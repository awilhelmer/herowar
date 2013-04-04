###
eventbus as EventBus
	@author Alexander Wilhelmer
###
eventbus = 
	rendererChanged: new signals.Signal()
	sceneChanged: new signals.Signal()
	objectAdded: new signals.Signal()
	objectSelected: new signals.Signal()
	objectChanged: new signals.Signal()
	materialChanged: new signals.Signal()
	clearColorChanged: new signals.Signal()
	cameraChanged: new signals.Signal()
	windowResize: new signals.Signal()
return eventbus