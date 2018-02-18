
def login(callback) {
//    log.trace "login:$callback"
    eeroCommandPost '/2.2/login/refresh', null, "loginTest", "text/json", callback
}

def loginTest(response, data) {
    log.trace "loginTest:$response.status:${data.get('passthru')}"
    log.trace "loginTest:$response.body"
    if (response.status == 200 && !response.hasError()) {
        // logged in, continue to passthru method
        log.trace "already logged in, calling passthru callback handler: ${data.get('passthru')}:${data.get('callback')}"
        data.callback = data.passthru
        eeroCommandGetResponseHandler(response, data)
    } else {
        log.trace "not logged in, redirecting to login start"
        eeroCommandPost '/2.2/login/refresh', null, "login", "text/json", data.get('passthru')
    }
}

def loginStart(response, data) {
//    log.trace "loginStart:$response.status:${data.get('passthru')}"
    eeroCommandPost('/signin', [
            "user[username]"    : kevoUsername,
            "user[password]"    : kevoPassword,
            "authenticity_token": state.token,
            "commit"            : "LOGIN",
            "utf8"              : "✓"
    ], 'loginCredentials', 'text/html', data.get('passthru'))
}

def loginCredentials(response, data) {
//    log.trace "loginCredentials:$response.status"
    if (response.status == 302 || response.status == 200) {
        log.trace "redirect or 200 from login post, considering successful"
    }
    eeroCommandGet('/user/locks', null, data.get('passthru'), 'text/html')
}


def refresh() {
    state.retry_count = 0
    sendEvent name: 'lock', value: 'refreshing'
    executeRefresh()
}

def autoRefresh() {
    state.retry_count = 0
    executeRefresh()
}

def executeRefresh() {
    log.trace "refresh:$state.retry_count"
    if (state.retry_count <= MAX_REFRESH_RETRIES) {
        state.retry_count = state.retry_count + 1
        login 'refreshSendCommand'
    } else {
        log.error "Too many attempts to retry refresh"
    }
}

def refreshSendCommand(response, data) {
//    log.trace "refreshSendCommand:$response.status"
//    log.trace "current bolt: $state.last_bolt_time && $state.bolt_state_time"
    eeroCommandGet("/user/remote_locks/command/lock.json", ['arguments': kevoLockId], "refreshVerifyCommand", "text/json")
}

def refreshVerifyCommand(response, data) {
//    log.trace "refreshVerifyCommand:$response.status->$state.bolt_state_time::::$state.last_bolt_time"
    if (response.status != 200) {
        log.error "couldn't determine lock status after refreshing, $e"
        sendEvent name: 'lock', value: 'unknown'
        return
    }

    if (state.bolt_state_time == state.last_bolt_time) {
        log.trace "Retrying since we have the old state"

        // the operation hasn't completed, retry the refresh in LOCK_REFRESH_WAIT seconds
        runIn LOCK_REFRESH_WAIT, executeRefresh
        return
    }
}


__EERO_CLIENT__

__DEVICE_LIFECYCLE_HOOKS__
