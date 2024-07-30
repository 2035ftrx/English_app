package org.example.studyenglishjava.ai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.example.studyenglishjava.auth.UserPrincipal;
import org.example.studyenglishjava.dto.AITokenResponse;
import org.example.studyenglishjava.entity.AIToken;
import org.example.studyenglishjava.entity.AITokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/app/ai")
public class AIInfoController {

    @Autowired
    private AITokenRepository aiTokenRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    @GetMapping("audioToken")
    public AITokenResponse generateAudioToken() throws JsonProcessingException {

        // Get the authenticated user from SecurityContext
        UserPrincipal currentUser = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Fetch JSON data from external platform
        val app_key = "LReKOGTyWd80t3dQbm9ROT2M";
        val secret_key = "Qy6DUahOx2UPAU8VPZapx4aOnqXymuD6";
        val token = fetchExternalData(app_key, secret_key);

        val aiToken = token.getSecond();
        aiToken.setUserId(currentUser.userId());
        aiTokenRepository.save(aiToken);

        return token.getFirst();
    }

    @GetMapping("aiToken")
    public AITokenResponse generateAIToken() throws JsonProcessingException {

        // Get the authenticated user from SecurityContext
        UserPrincipal currentUser = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Fetch JSON data from external platform
        val app_key = "WQjZgCFW7pfiuZrbjn3x5elU";
        val secret_key = "B0yHYRFa0ZrdRqGRvPCH1LrkqWQk8pn5";
        val token = fetchExternalData(app_key, secret_key);

        val aiToken = token.getSecond();
        aiToken.setUserId(currentUser.userId());
        aiTokenRepository.save(aiToken);

        return token.getFirst();
    }

    private Pair<AITokenResponse, AIToken> fetchExternalData(String app_key, String secret_key) throws JsonProcessingException {
        // Replace with the actual external API URL
        String apiUrl = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=" + app_key +
                "&client_secret=" + secret_key;

        // Create a RestTemplate object
        RestTemplate restTemplate = new RestTemplate();

        // Make the HTTP GET request
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

        // Check the response status code
        if (response.getStatusCode() == HttpStatus.OK) {
            val jsonData = response.getBody();
            val aiTokenResponse = mapper.readValue(jsonData, AITokenResponse.class);

            val aiToken = new AIToken();
            aiToken.setToken(jsonData);
            // aiToken.setUserId(currentUser.userId());
            aiToken.setCreatedAt(System.currentTimeMillis());
            return Pair.of(aiTokenResponse, aiToken);
        } else {
            throw new RuntimeException("Error fetching data from external API: " + response.getStatusCode());
        }
    }
}
