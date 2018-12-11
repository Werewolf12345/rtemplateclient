function getuserdata(url) {
    $.ajax({
        url: url
    }).then(function(data) {
        $('.user-id').append(data.id);
        $('.user-username').append(data.username);
        $('.user-firstname').append(data.firstName);
        $('.user-lastname').append(data.lastName);
        $('.user-email').append(data.email);
        $('.user-roles').append(data.roles.map(function (item) {
            return item['role'].replace('ROLE_', '');
        }).join(', '));
    });
}

function userstable() {
    $.ajax({
        url: '/users',
        success(response) {
            var trHTML = '';
            $.each(response, function (i, item) {
                trHTML += '<tr class=\'clickable-row\' data-href=\'/user/' + item.id + '\''
                    + '><td>' + item.id + '</td><td>' + item.firstName + '</td><td>' + item.lastName
                    + '</td><td>' + item.email + '</td></tr>';
            });
            $('#userstable').append(trHTML);
        }
    });


}

