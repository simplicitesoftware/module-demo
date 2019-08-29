var demo = typeof demo !== 'undefined' ? demo : (function($) {
	var app, prd;
	
	/**
	 * Render
	 * @param root Context path
	 * @param banner Banner URL
	 * @param pub Public?
	 * @function
	 */
	function render(root, banner, pub) {
		if (!pub) $("#demo").css("min-height", "1000px");
		app = pub ? new Simplicite.Ajax(root, 'api', 'website', 'simplicite') : new Simplicite.Ajax();
		prd = app.getBusinessObject('DemoProduct');
		prd.bannerURL = banner; // Image banner URL
		prd.toFixed = function() { return function(n, r) { return parseFloat(r(n)).toFixed(2); } }; // Rendering for decimal
		prd.search(function() {
			$('#demo').html(Mustache.render($('#demo-template').html(), prd));
		}, null, { inlineDocs: true });
	}

	return { render: render };	
})(jQuery);
