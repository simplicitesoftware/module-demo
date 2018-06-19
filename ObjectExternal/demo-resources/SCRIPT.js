if (typeof demo === 'undefined') demo = (function($) {
	var app, prd;
	
	function render(root, banner) {
		app = new Simplicite.Ajax(root, 'api', 'website', 'simplicite');
		prd = app.getBusinessObject('DemoProduct');
		prd.toFixed = function() { return function(n, r) { return parseFloat(r(n)).toFixed(2); } }; // Mustache rendering for decimal
		prd.bannerURL = banner; // Image banner URL for Mustache rendering
		prd.search(function() {
			$('#demo').html(Mustache.render($('#demo-template').html(), prd));
		}, null, { inlineDocs: true });
	}

	return { render: render };	
})(jQuery);
