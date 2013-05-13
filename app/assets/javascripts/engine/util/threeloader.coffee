class JSONLoader extends THREE.JSONLoader
	
	#here we can use the loader and can callback the original json object ... 
	loadAjaxJSON: (context, url, callback, texturePath, callbackProgress) ->
		xhr = new XMLHttpRequest()
		length = 0
		xhr.onreadystatechange = ->
				if xhr.readyState is xhr.DONE 
					if xhr.status is 200 or xhr.status is 0  
						if xhr.responseText
							json = JSON.parse(xhr.responseText)
							scale = json.scale
							json.scale = 1
							result = context.parse json, texturePath
							json.scale = scale
							callback result.geometry, result.materials, json
						else
							console.warn "JSONLoader: [#{url}] seems to be unreachable or file there is empty" 
						context.onLoadComplete()
					else
						console.error  "JSONLoader: Couldn't load [#{url}] [#{xhr.status}]" 
				else if xhr.readyState is xhr.LOADING
					if callbackProgress
						if length is 0
							length = xhr.getResponseHeader( "Content-Length" )
						callbackProgress(total: length, loaded: xhr.responseText.length)
				else if xhr.readyState is xhr.HEADERS_RECEIVED
					if callbackProgress is not undefined
						length = xhr.getResponseHeader "Content-Length"
				null
		
		xhr.open "GET", url, true
		xhr.withCredentials = @withCredentials
		xhr.send null
		
return JSONLoader