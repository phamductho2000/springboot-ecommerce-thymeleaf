$().ready(function () {
    $("#customerForm").validate({
        onfocusout: false,
        onkeyup: false,
        onclick: false,
        rules: {
            "customerId": {
                required: true,
                min: 1
            },
        },
        messages: {
            "customerId": {
                required: "Thông tin bắt buộc nhập",
                min: "Vui lòng chọn khách hàng"
            },
        }
    });
});
