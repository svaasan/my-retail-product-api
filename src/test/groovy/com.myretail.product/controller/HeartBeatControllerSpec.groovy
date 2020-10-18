package com.myretail.product.controller

import spock.lang.Specification

class HeartBeatControllerSpec extends Specification {
    def "Successful heartbeat"(){
        given:
        HeartBeatController controller = new HeartBeatController(appName:"app name", appVersion: "version")

        when:
        Map<String, String>  heartBeatResponse = controller.getHeartBeat()

        then:
        heartBeatResponse != null
        heartBeatResponse.name == "app name"
        heartBeatResponse.version == "version"
    }
}
