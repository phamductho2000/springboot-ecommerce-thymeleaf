$(".btn-add-to-cart").click(function () {
    let productId = $(this).parent().find('.product-id').val()
    $.ajax({
        url: `/them-vao-gio-hang/${productId}`,
        type: 'POST',
        data: {quantity: 1},
        success: function (response) {
            console.log(response);
            $('.cart-items').replaceWith($(response).find('.cart-items'));
            var myToastEl = document.getElementById('liveToast')
            var myToast = new bootstrap.Toast(myToastEl)
            myToast.show();
        },
        error: function (error) {
            console.log(error);
        }
    })
})

