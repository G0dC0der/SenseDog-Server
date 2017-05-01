package com.sensedog;

import com.sensedog.repository.entry.Detection;
import com.sensedog.rest.client.AlarmResourceClient;
import com.sensedog.rest.client.MasterUserResourceClient;
import com.sensedog.rest.client.RestResponse;
import com.sensedog.rest.entry.request.AlarmCreateRequest;
import com.sensedog.rest.entry.request.ConnectRequest;
import com.sensedog.rest.entry.request.DetectRequest;
import com.sensedog.rest.entry.request.MasterUserCreateRequest;
import com.sensedog.rest.entry.response.ServiceCreateResponse;
import com.sensedog.rest.entry.response.ServicesResponse;
import com.sensedog.rest.entry.response.SeverityResponse;
import com.sensedog.rest.entry.response.TokenResponse;
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
        AlarmCreateRequest alarm1 = EntryUtil.alarmCreateRequest();
        AlarmCreateRequest alarm2 = EntryUtil.alarmCreateRequest();
        AlarmCreateRequest alarm3 = EntryUtil.alarmCreateRequest();
        RestResponse<ServiceCreateResponse> create1Resp = alarmClient.create(alarm1);
        RestResponse<ServiceCreateResponse> create2Resp = alarmClient.create(alarm2);
        RestResponse<ServiceCreateResponse> create3Resp = alarmClient.create(alarm3);
        assertTrue(create1Resp.isOk());
        assertTrue(create2Resp.isOk());
        assertTrue(create3Resp.isOk());

        MasterUserCreateRequest master = EntryUtil.masterUserCreateRequest();
        RestResponse<Void> masterResp = masterClient.newMasterUser(master);
        assertTrue(masterResp.isOk());

        ConnectRequest con1 = new ConnectRequest();
        con1.setPinCode(create1Resp.entity.getPinCode());
        con1.setEmail(master.getEmail());
        ConnectRequest con2 = new ConnectRequest();
        con2.setPinCode(create2Resp.entity.getPinCode());
        con2.setEmail(master.getEmail());
        ConnectRequest con3 = new ConnectRequest();
        con3.setPinCode(create3Resp.entity.getPinCode());
        con3.setEmail(master.getEmail());
        RestResponse<TokenResponse> con1Resp = masterClient.connect(con1);
        RestResponse<TokenResponse> con2Resp = masterClient.connect(con2);
        RestResponse<TokenResponse> con3Resp = masterClient.connect(con3);
        assertTrue(con1Resp.isOk());
        assertTrue(con2Resp.isOk());
        assertTrue(con3Resp.isOk());

        DetectRequest detectRequest = EntryUtil.detectRequest();
        RestResponse<SeverityResponse> detectionResp = alarmClient.detection(create1Resp.entity.getAlarmAuthToken(), detectRequest);
        assertTrue(detectionResp.isOk());

        RestResponse<ServicesResponse> viewResp = masterClient.viewAll(master.getEmail());
        assertTrue(viewResp.isOk());
        assertTrue(viewResp.entity.getServices().stream().map(ServicesResponse.Service::getServiceName).map(Object::toString).anyMatch(s -> s.equals(alarm1.getServiceName())));
        assertTrue(viewResp.entity.getServices().stream().map(ServicesResponse.Service::getServiceName).map(Object::toString).anyMatch(s -> s.equals(alarm2.getServiceName())));
        assertTrue(viewResp.entity.getServices().stream().map(ServicesResponse.Service::getServiceName).map(Object::toString).anyMatch(s -> s.equals(alarm3.getServiceName())));

        assertTrue(viewResp.entity.getServices().stream().mapToInt(ServicesResponse.Service::getNumberOfDetections).count() > 0);
    }
}
