package com.crcl.post.clients;

import com.crcl.post.configuration.Oauth2.FeignFormConfig;
import com.crcl.post.configuration.Oauth2.OAuthFeignConfig;
import com.crcl.post.dto.ProfileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

// TODO: 1/8/2023 add resiliency
@FeignClient(name = "${client.profile.name}", url = "${client.profile.url}", configuration = {OAuthFeignConfig.class, FeignFormConfig.class})
public interface ProfileClient {
    @GetMapping("/profiles/profile/username/{username}")
    ProfileDto findByUsername(@PathVariable String username);

    @GetMapping("/profiles/profile/usernames")
    List<ProfileDto> findByUsernames(@RequestParam("usernames") Set<String> userNames);
}
