package com.sscs.apitest.pinpad.api;

import com.google.gson.Gson;
import com.sscs.apitest.pinpad.config.PinpadConfig;
import com.sscs.apitest.pinpad.secure.PinpadSecurity;
import com.sscs.apitest.pinpad.service.CardMDbService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class PinpadApiTest {

    private static final Gson gson = new Gson();
    @Autowired
    private CardMDbService cardMDbService;

    @Autowired
    private PinpadConfig config;

    @PostMapping("/genPinblock")
    public String genPinblock(@RequestBody String body) throws Exception {
        log.debug("===== REQUEST =======\n" + body);

//        JsonUtil jsonUtil = new JsonUtil();
        Map<String, Object> bodyMap = gson.fromJson(body, Map.class);
        String pin = (String) bodyMap.get("pin");
        String sessionKey = (String) bodyMap.get("sessionKey");
        String card = (String) bodyMap.get("card");
        String term = (String) bodyMap.get("term");
        String termkey = config.getTermKey(term);

        String clear = PinpadSecurity.decryptToHex(termkey, sessionKey);
//
        String pinblock = PinpadSecurity.encryptPinAnsi(clear, card, pin);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pinblock", pinblock);
        map.put("response_code", "SUCCESS");

        String json = gson.toJson(map);
        log.debug("===== RESPONSE =======\n" + json);
        log.debug("======================\n");
        return json;
    }

    @PostMapping("/resetCardToInitPin")
    public String resetCardToInitPin(@RequestBody String body) throws Exception {
        log.debug("===== REQUEST =======\n" + body);

//        JsonUtil jsonUtil = new JsonUtil();
        Map<String, Object> bodyMap = gson.fromJson(body, Map.class);
        String card = (String) bodyMap.get("card");

        cardMDbService.resetCardToInitPin(card);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("response_code", "SUCCESS");

        String json = gson.toJson(map);
        log.debug("===== RESPONSE =======\n" + json);
        log.debug("======================\n");
        return json;
    }

}
