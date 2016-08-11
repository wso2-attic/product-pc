$("#import_process_form").on("submit", function (e) {
    e.preventDefault();
    if($('#processZip').val().length === 0) {
        alertify.error('no process archive selected');
    } else {
        $('#import_process_form').ajaxSubmit({
            type: "POST",
            url: "/publisher/assets/process/apis/import_process",
            data: $('#import_process_form').serialize(),
            cache: false,
            success: function (data) {
                var response = JSON.parse(data);
                if (response.error === true) {
                    alertify.error(response.content);
                }
                else {
                    alertify.success("Successfully imported process");
                    window.location.href = '../process/list';
                }
            }
        });
    }

})