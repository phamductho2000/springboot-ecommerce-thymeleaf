$(function () {
    "use strict";

    const tab1 = document.querySelector('#product-tab #info-tab')
    const tab2 = document.querySelector('#product-tab #attr-tab')

    var step = 1;

    $('#next-step').on('click', function () {
        if (step == 1 && $('#product-form').valid()) {
            step = 2;
            var secondTab = new bootstrap.Tab(tab2);
            secondTab.show();
            $('#next-step').css('display', 'none');
            $('#submit-form').css('display', 'inline-block');
            return;
        }
        if (step == 2) {
            return;
        }
    })

    $('#back-step').on('click', function () {
        if (step == 2) {
            step = 1;
            var firstTab = new bootstrap.Tab(tab1)
            firstTab.show();
            $('#next-step').css('display', 'inline-block');
            $('#submit-form').css('display', 'none');
            return;
        }
        if (step == 1) {
            return;
        }
    })

});
