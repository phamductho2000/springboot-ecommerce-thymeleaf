function addItemtoCart(productId, quantity) {
    if (productId) {
        $.ajax({
            url: `/them-vao-gio-hang/${productId}`,
            type: 'POST',
            data: {quantity: 1},
            success: function (response) {
                $('#header-cart').replaceWith($(response).find('#header-cart'));
                var myToastEl = document.getElementById('alert-toast');
                $('#alert-toast-content').text('Đã thêm vào giỏ hàng');
                var myToast = new bootstrap.Toast(myToastEl);
                myToast.show();
            },
            error: function (error) {
                console.log(error);
            }
        })
    }
}

function updateCart(id) {
    var quantity = $(`#inputQuantity${id}`).val();
    $.ajax({
        url: `/cap-nhat-gio-hang/${id}`,
        type: 'POST',
        data: {quantity: quantity},
        success: function (response) {
            $('#header-cart').replaceWith($(response).find('#header-cart'));
            $('.cart-list-head').replaceWith($(response).find('.cart-list-head'));
            $('.right').replaceWith($(response).find('.right'));
            $("input[type='number']").inputSpinner();
            var myToastEl = document.getElementById('alert-toast');
            $('#alert-toast-content').text('Đã cập nhật giỏ hàng');
            var myToast = new bootstrap.Toast(myToastEl);
            myToast.show();
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function removeItemFromCart(id) {
    $.ajax({
        url: `/xoa-khoi-gio-hang/${id}`,
        type: 'POST',
        success: function (response) {
            $('#header-cart').replaceWith($(response).find('#header-cart'));
            $('.shopping-cart').replaceWith($(response).find('.shopping-cart'));
            $("input[type='number']").inputSpinner();
            var myToastEl = document.getElementById('alert-toast');
            $('#alert-toast-content').text('Đã xóa sản phẩm khỏi giỏ hàng');
            var myToast = new bootstrap.Toast(myToastEl);
            myToast.show();
        },
        error: function (error) {
            console.log(error);
        }
    })
}

