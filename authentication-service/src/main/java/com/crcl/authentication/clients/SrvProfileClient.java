package com.crcl.authentication.clients;

import com.crcl.authentication.configuration.clients.SrvOauth2ClientConfig;
import com.crcl.authentication.dto.ProfileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// TODO: 1/8/2023 add resiliency
@FeignClient(name = "${client.srvProfile.name}", url = "${client.profile.url}",
        configuration = {SrvOauth2ClientConfig.class})
public interface SrvProfileClient {
    @GetMapping("/profiles/profile/username/{username}")
    ProfileDto findByUsername(@PathVariable String username);
}
