$().ready(function () {
    $("#product-form").validate({
        onfocusout: false,
        onkeyup: false,
        onclick: false,
        rules: {
            "name": {
                required: true,
            },
            "shortDes": {
                required: true,
            },
            "detailDes": {
                required: true,
            },
            "category": {
                required: true,
            },
            "inputOriginalPrice": {
                required: true,
                // min: 1,
                // number: true,
            },
            "inputPrice": {
                required: true,
                // min: 1
            },
            "quantity": {
                required: true,
                min: 1
            },
            "category": {
                required: true,
                min: 1
            }
        },
        messages: {
            "name": {
                required: "Thông tin bắt buộc nhập",
            },
            "shortDes": {
                required: "Thông tin bắt buộc nhập",
            },
            "detailDes": {
                required: "Thông tin bắt buộc nhập",
            },
            "category": {
                required: "Thông tin bắt buộc nhập",
            },
            "inputOriginalPrice": {
                required: "Thông tin bắt buộc nhập",
                // min: "Giá nhập phải > 0",
                // number: "Giá trị nhập phải là số"
            },
            "inputPrice": {
                required: "Thông tin bắt buộc nhập",
                // min: "Giá bán phải > 0"
            },
            "quantity": {
                required: "Thông tin bắt buộc nhập",
                min: "Số lượng phải > 0"
            },
            "category": {
                required: "Thông tin bắt buộc nhập",
                min: "Thông tin bắt buộc nhập"
            }
        }
    });
});
