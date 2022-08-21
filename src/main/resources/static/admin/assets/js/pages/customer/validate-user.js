$().ready(function () {
    $("#user-form").validate({
        onfocusout: false,
        onkeyup: false,
        onclick: false,
        rules: {
            "name": {
                required: true,
            },
            "username": {
                required: true,
            },
            "email": {
                required: true,
                email: true
            },
            "password": {
                required: true,
                min: 6,
            },
            "re-password": {
                required: true,
                min: 6,
                equalTo: "#input-password",
            }
        },
        messages: {
            "name": {
                required: "Thông tin bắt buộc nhập",
            },
            "username": {
                required: "Thông tin bắt buộc nhập",
            },
            "email": {
                required: "Thông tin bắt buộc nhập",
                email: "Không đúng định dạng email"
            },
            "password": {
                required: "Thông tin bắt buộc nhập",
                min: "Mật khẩu phải trên 6 ký tự"
            },
            "re-password": {
                required: "Thông tin bắt buộc nhập",
                min: "Mật khẩu phải trên 6 ký tự",
                equalTo: "Mật khẩu không khớp"
            }
        }
    });
});
