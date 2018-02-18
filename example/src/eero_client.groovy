def w_http_client_base_uri(method, path, body, callback, contentType, passthru) {
    'https://api-user.eero.com/2.2/'
}

def w_http_client_update_token(response, data) {
    w_http_default_token_cookie(response)
}

def w_http_client_post_callback(callback, response, data) {
    switch (callback) {
        case "loginTest": loginTest(response, data); break
        case "loginStart": loginStart(response, data); break
        case "loginCredentials": loginCredentials(response, data); break
        case "refreshSendCommand": refreshSendCommand(response, data); break
        case "refreshVerifyCommand": refreshVerifyCommand(response, data); break
        default: noCallback(response, data); break
    }

    log.info "POST Callback with callback: $callback, and data: $data"
}

def w_http_client_get_callback(callback, response, data) {
    switch (callback) {
        case "loginTest": loginTest(response, data); break
        case "loginStart": loginStart(response, data); break
        case "loginCredentials": loginCredentials(response, data); break
        case "refreshSendCommand": refreshSendCommand(response, data); break
        case "refreshVerifyCommand": refreshVerifyCommand(response, data); break
        default: noCallback(response, data); break
    }

    log.info "GET Callback with callback: $callback, and data: $data"
}

def w_http_client_referrer(path, body, callback, contentType) {
    'https://api-user.eero.com/2.2/'
}

__HTTP__
