package com.sensedog;

import com.sensedog.rest.client.AlarmResourceClient;
import com.sensedog.rest.client.MasterUserResourceClient;
import com.sensedog.rest.client.RestResponse;
import com.sensedog.rest.model.request.ApiAlarmCreate;
import com.sensedog.rest.model.request.ApiConnect;
import com.sensedog.rest.model.request.ApiDetect;
import com.sensedog.rest.model.request.ApiMasterUserCreate;
import com.sensedog.rest.model.ApiServiceCreate;
import com.sensedog.rest.model.ApiServices;
import com.sensedog.rest.model.ApiSeverity;
import com.sensedog.rest.model.ApiToken;
import com.sensedog.test.EntryUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MasterServiceTest {

    private MasterUserResourceClient masterClient;
    private AlarmResourceClient alarmClient;

    @Before
    public void init() {
        masterClient = new MasterUserResourceClient("http://localhost:8080/dog");
        alarmClient = new AlarmResourceClient("http://localhost:8080/dog");
    }

    @Test
    public void registerAndViewMultipleServicesToOneMaster() { //TODO: Test with detections as well.
        final ApiAlarmCreate alarm1 = EntryUtil.alarmCreateRequest();
        final ApiAlarmCreate alarm2 = EntryUtil.alarmCreateRequest();
        final ApiAlarmCreate alarm3 = EntryUtil.alarmCreateRequest();
        final RestResponse<ApiServiceCreate> create1Resp = alarmClient.create(alarm1);
        final RestResponse<ApiServiceCreate> create2Resp = alarmClient.create(alarm2);
        final RestResponse<ApiServiceCreate> create3Resp = alarmClient.create(alarm3);
        assertTrue(create1Resp.isOk());
        assertTrue(create2Resp.isOk());
        assertTrue(create3Resp.isOk());

        final ApiMasterUserCreate master = EntryUtil.masterUserCreateRequest();
        final RestResponse<Void> masterResp = masterClient.newMasterUser(master);
        assertTrue(masterResp.isOk());

        final ApiConnect con1 = new ApiConnect();
        con1.setPinCode(create1Resp.entity.getPinCode());
        con1.setEmail(master.getEmail());
        final ApiConnect con2 = new ApiConnect();
        con2.setPinCode(create2Resp.entity.getPinCode());
        con2.setEmail(master.getEmail());
        final ApiConnect con3 = new ApiConnect();
        con3.setPinCode(create3Resp.entity.getPinCode());
        con3.setEmail(master.getEmail());
        final RestResponse<ApiToken> con1Resp = masterClient.connect(con1);
        final RestResponse<ApiToken> con2Resp = masterClient.connect(con2);
        final RestResponse<ApiToken> con3Resp = masterClient.connect(con3);
        assertTrue(con1Resp.isOk());
        assertTrue(con2Resp.isOk());
        assertTrue(con3Resp.isOk());

        final ApiDetect detectRequest = EntryUtil.detectRequest();
        final RestResponse<ApiSeverity> detectionResp = alarmClient.detection(create1Resp.entity.getAlarmAuthToken(), detectRequest);
        assertTrue(detectionResp.isOk());

        final RestResponse<ApiServices> viewResp = masterClient.viewAll(master.getEmail());
        assertTrue(viewResp.isOk());
        assertTrue(viewResp.entity.getServices().stream().map(ApiServices.Service::getServiceName).map(Object::toString).anyMatch(s -> s.equals(alarm1.getServiceName())));
        assertTrue(viewResp.entity.getServices().stream().map(ApiServices.Service::getServiceName).map(Object::toString).anyMatch(s -> s.equals(alarm2.getServiceName())));
        assertTrue(viewResp.entity.getServices().stream().map(ApiServices.Service::getServiceName).map(Object::toString).anyMatch(s -> s.equals(alarm3.getServiceName())));

        assertTrue(viewResp.entity.getServices().stream().mapToInt(ApiServices.Service::getNumberOfDetections).count() > 0);
    }
}
