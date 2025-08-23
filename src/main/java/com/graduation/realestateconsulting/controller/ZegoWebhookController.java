    package com.graduation.realestateconsulting.controller;


    import com.graduation.realestateconsulting.services.ZegoCallbackService;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    @Slf4j
    @RestController
    @RequiredArgsConstructor
    @RequestMapping("/api/callbacks")
    public class ZegoWebhookController {
        private final ZegoCallbackService zegoCallbackService;

//        @Value("${zegocloud.webhook.secret}")
//        private String zegoSecret;

        @PostMapping("/zegocloud")
        public ResponseEntity<String> handleZegoCallback(@RequestBody String rawPayload) {

            try {
                zegoCallbackService.processZegoCallback(rawPayload);
                return ResponseEntity.ok("Received");
            } catch (Exception e) {
                log.error("Error processing ZegoCloud payload", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing payload");
            }
        }
    }
