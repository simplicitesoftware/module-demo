//----------------------------------------------------
// Client-side logic for customer business object
//----------------------------------------------------
var DemoClient = typeof DemoClient !== "undefined" ? DemoClient : (function($) {
	// Display Google map
	var _map = function() {
		var rowId = getFieldValue("row_id");
		if (rowId && rowId != "0") {
			var n = $.escapeHTML(getFieldValue("demoCliFirstname") + " " + getFieldValue("demoCliLastname"));
			var a = $.escapeHTML(getFieldValue("demoCliAddress1") + ", " + getFieldValue("demoCliZipCode") + " " + getFieldValue("demoCliCity") + ", " + getFieldValue("demoCliCountry"));
			var c = getFieldValue("demoCliCoords");
			if (c !== "") {
				var l = c.replace(";", ",").split(",");
				var u = Simplicite.ROOT + "/googlemap?lat=" + l[0] + "&lng=" + l[1] + "&info=" + encodeURIComponent("<div style=\"width: 200px; height: 75px;\"><b>" + n + "</b><br/>" + a + "</div>");
				$("#client-map").show();
				window.frames.gmap.location.replace(u + "&width=500&height=450&title=false");
			}
			return false;
		}
	};

	// Display Google Hangouts button
	var _hangout = function() {
		var email = getFieldValue("demoCliEmail");
		var invites = email ? [{ id: email, invite_type: "EMAIL" }]: [];
		gapi.hangout.render("hangout-button", { "render": "createhangout", "invites": invites });
	};

	// Responsive UI hook
	Simplicite.UI.hooks.DemoClient = function(o, cbk) {
		// Minimalistic backward compatibility with legacy UI getFieldValue for responsive UI
		window.getFieldValue = function(name) { return $ui.getUIField(null, o, name).ui.val(); };

		try {
			// Display Hangout buttons on form load
			o.locals.ui.form.onload = function() {
				try {
					if (typeof gapi === "undefined" || typeof gapi.hangout === "undefined") {
						$ui.loadScript({
							url: "https://apis.google.com/js/platform.js",
							onload: _hangout
						})
					} else {
						_hangout();
					}
				} catch(el) {
					console.error(el);
				}
			}
		} catch(e) {
			console.error(e.message);
		} finally {
			cbk && cbk();
		}
	};

	return {
		// Action function
		map: function() {
			if (typeof google === "undefined" || typeof google.maps === "undefined") {
				$ui.loadScript({
					url: Simplicite.GOOGLE_MAPS_JS_URL,
					onload: _map
				});
			} else {
				_map();
			}
		}
	}
})(jQuery);