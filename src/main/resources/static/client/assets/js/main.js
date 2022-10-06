/*
Template Name: ShopGrids - Bootstrap 5 eCommerce HTML Template.
Author: GrayGrids
*/

(function () {
    //===== Prealoder

    window.onload = function () {
        window.setTimeout(fadeout, 500);
    }

    function fadeout() {
        document.querySelector('.preloader').style.opacity = '0';
        document.querySelector('.preloader').style.display = 'none';
    }

    /*=====================================
    Sticky
    ======================================= */
    window.onscroll = function () {
        var header_navbar = document.querySelector(".navbar-area");
        var sticky = header_navbar.offsetTop;

        // show or hide the back-top-top button
        var backToTo = document.querySelector(".scroll-top");
        if (document.body.scrollTop > 50 || document.documentElement.scrollTop > 50) {
            backToTo.style.display = "flex";
        } else {
            backToTo.style.display = "none";
        }
    };

    //===== mobile-menu-btn
    let navbarToggler = document.querySelector(".mobile-menu-btn");
    navbarToggler.addEventListener('click', function () {
        navbarToggler.classList.toggle("active");
    });

    //
    // $("#min_price,#max_price").on('change', function () {
    //
    //     $('#price-range-submit').show();
    //
    //     var min_price_range = parseInt($("#min_price").val());
    //
    //     var max_price_range = parseInt($("#max_price").val());
    //
    //     if (min_price_range > max_price_range) {
    //         $('#max_price').val(min_price_range);
    //     }
    //
    //     $("#slider-range").slider({
    //         values: [min_price_range, max_price_range]
    //     });
    //
    // });
    //
    //
    // $("#min_price,#max_price").on("paste keyup", function () {
    //
    //     $('#price-range-submit').show();
    //
    //     var min_price_range = parseInt($("#min_price").val());
    //
    //     var max_price_range = parseInt($("#max_price").val());
    //
    //     if (min_price_range == max_price_range) {
    //
    //         max_price_range = min_price_range + 100;
    //
    //         $("#min_price").val(min_price_range);
    //         $("#max_price").val(max_price_range);
    //     }
    //
    //     $("#slider-range").slider({
    //         values: [min_price_range, max_price_range]
    //     });
    //
    // });
    //
    // $(function () {
    //
    //     $("#min_price").val($("#slider-range").slider("values", 0));
    //     $("#max_price").val($("#slider-range").slider("values", 1));
    // });
    //
    // $("#min_price").val($("#slider-range").slider("values", 0));
    // $("#max_price").val($("#slider-range").slider("values", 1));



})();